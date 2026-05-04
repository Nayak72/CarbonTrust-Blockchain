import math
from app.core.supabase_client import get_supabase
from app.utils.logger import get_logger
from app.config import settings

logger = get_logger(__name__)


async def check_anomaly(
    sensor_id: str,
    facility_id: str,
    co2_ppm: float,
    reading_id: str
) -> dict | None:
    """
    Runs anomaly detection on a new CO2 reading.
    Returns anomaly dict if detected, None if clean.

    Checks (in order):
    1. Zero reading — sensor reporting 0 ppm (hardware failure)
    2. Frozen value — same reading N consecutive times (stuck sensor)
    3. Z-score breach — statistically unusual value (> threshold std devs from mean)
    """
    supabase = get_supabase()

    # --- Check 1: Zero reading ---
    if co2_ppm == 0:
        logger.warning(f"Zero reading detected on sensor {sensor_id}")
        return {
            "sensor_id": sensor_id,
            "facility_id": facility_id,
            "reading_id": reading_id,
            "anomaly_type": "zero_reading",
            "co2_value": co2_ppm,
            "z_score": None
        }

    # --- Fetch last 100 clean readings for this sensor ---
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
            return {
                "sensor_id": sensor_id,
                "facility_id": facility_id,
                "reading_id": reading_id,
                "anomaly_type": "frozen_value",
                "co2_value": co2_ppm,
                "z_score": None
            }

    # --- Check 3: Z-score ---
    if len(historical) < 10:
        # Not enough history for statistical check — pass through
        return None

    mean = sum(historical) / len(historical)
    variance = sum((x - mean) ** 2 for x in historical) / len(historical)
    std_dev = math.sqrt(variance) if variance > 0 else 0

    if std_dev == 0:
        return None

    z_score = abs((co2_ppm - mean) / std_dev)

    if z_score > settings.ANOMALY_ZSCORE_THRESHOLD:
        logger.warning(
            f"Z-score anomaly on sensor {sensor_id}: z={z_score:.2f}, value={co2_ppm}"
        )
        return {
            "sensor_id": sensor_id,
            "facility_id": facility_id,
            "reading_id": reading_id,
            "anomaly_type": "zscore_breach",
            "co2_value": co2_ppm,
            "z_score": round(z_score, 4)
        }

    return None  # Clean reading
