import math
from app.core.supabase_client import get_supabase
from app.utils.logger import get_logger
from app.config import settings

logger = get_logger(__name__)


def _compute_moving_average(readings: list[float], window: int = 10) -> list[float]:
    """
    Compute a sliding-window moving average over a list of CO₂ readings.
    Returns a list of the same length; the first (window-1) values are simple
    cumulative averages where fewer than `window` samples are available.
    """
    result = []
    for i in range(len(readings)):
        start = max(0, i - window + 1)
        chunk = readings[start : i + 1]
        result.append(sum(chunk) / len(chunk))
    return result


async def check_anomaly(
    sensor_id: str,
    facility_id: str,
    co2_ppm: float,
    reading_id: str,
    window_size: int = None
) -> tuple[dict | None, float | None]:
    """
    Runs anomaly detection on a new CO2 reading using a moving-average-first
    approach to comply with the analytical model.

    Returns a tuple: (anomaly_dict | None, c_avg_current | None)
      - anomaly_dict is set when an anomaly is detected; None when clean.
      - c_avg_current is always returned (needed for audit trail storage).

    Checks (in order):
    1. Zero reading — sensor reporting 0 ppm (hardware failure)
    2. Frozen value — same reading N consecutive times (stuck sensor)
    3. Spike — raw co2_ppm > c_avg_current × 3.0 (instantaneous spike before smoothing)
    4. Z-score on C_avg — statistically unusual moving-average value
    """
    if window_size is None:
        window_size = settings.MOVING_AVERAGE_WINDOW

    supabase = get_supabase()

    # --- Check 1: Zero reading ---
    if co2_ppm == 0:
        logger.warning(f"Zero reading detected on sensor {sensor_id}")
        return (
            {
                "sensor_id": sensor_id,
                "facility_id": facility_id,
                "reading_id": reading_id,
                "anomaly_type": "zero_reading",
                "co2_value": co2_ppm,
                "z_score": None,
                "c_avg": None,
            },
            None,
        )

    # --- Fetch last 100 clean readings for this sensor (most recent first) ---
    result = supabase.table("sensor_readings") \
        .select("co2_ppm") \
        .eq("sensor_id", sensor_id) \
        .eq("is_anomaly", False) \
        .order("timestamp", desc=True) \
        .limit(100) \
        .execute()

    historical = [r["co2_ppm"] for r in result.data] if result.data else []

    # --- Check 2: Frozen value ---
    if len(historical) >= settings.ANOMALY_FROZEN_COUNT_THRESHOLD:
        last_n = historical[:settings.ANOMALY_FROZEN_COUNT_THRESHOLD]
        if all(v == co2_ppm for v in last_n):
            logger.warning(f"Frozen value detected on sensor {sensor_id}: {co2_ppm}")
            return (
                {
                    "sensor_id": sensor_id,
                    "facility_id": facility_id,
                    "reading_id": reading_id,
                    "anomaly_type": "frozen_value",
                    "co2_value": co2_ppm,
                    "z_score": None,
                    "c_avg": None,
                },
                None,
            )

    # --- Build the working window: new reading prepended to historical (oldest last) ---
    # historical is newest-first; we prepend the new reading → window is [new, h[0], h[1], ...]
    window_readings = [co2_ppm] + historical

    # Compute c_avg_current from the most recent `window_size` readings
    recent = window_readings[:window_size]
    c_avg_current = sum(recent) / len(recent)

    # --- Check 3: Spike — raw value is more than 3× the moving average ---
    if co2_ppm > c_avg_current * 3.0:
        logger.warning(
            f"Spike anomaly on sensor {sensor_id}: "
            f"raw={co2_ppm:.1f} > 3 × c_avg={c_avg_current:.1f}"
        )
        return (
            {
                "sensor_id": sensor_id,
                "facility_id": facility_id,
                "reading_id": reading_id,
                "anomaly_type": "spike",
                "co2_value": co2_ppm,
                "z_score": None,
                "c_avg": round(c_avg_current, 4),
            },
            round(c_avg_current, 4),
        )

    # --- Check 4: Z-score on moving averages ---
    if len(historical) < 10:
        # Not enough history for statistical check — pass through clean
        return None, round(c_avg_current, 4)

    # Compute moving averages across all historical readings + current
    all_readings = [co2_ppm] + historical  # newest first → reverse for time order
    avg_series = _compute_moving_average(list(reversed(all_readings)), window=window_size)

    # c_avg_current is the last element (most recent)
    c_avg_stat = avg_series[-1]  # should match c_avg_current

    mean = sum(avg_series) / len(avg_series)
    variance = sum((x - mean) ** 2 for x in avg_series) / len(avg_series)
    std_dev = math.sqrt(variance) if variance > 0 else 0

    if std_dev == 0:
        return None, round(c_avg_current, 4)

    z_score = abs((c_avg_stat - mean) / std_dev)

    if z_score > settings.ANOMALY_ZSCORE_THRESHOLD:
        logger.warning(
            f"Z-score anomaly on sensor {sensor_id}: z={z_score:.2f}, "
            f"c_avg={c_avg_stat:.1f} (raw={co2_ppm})"
        )
        return (
            {
                "sensor_id": sensor_id,
                "facility_id": facility_id,
                "reading_id": reading_id,
                "anomaly_type": "zscore_breach",
                "co2_value": co2_ppm,
                "z_score": round(z_score, 4),
                "c_avg": round(c_avg_current, 4),
            },
            round(c_avg_current, 4),
        )

    return None, round(c_avg_current, 4)  # Clean reading
