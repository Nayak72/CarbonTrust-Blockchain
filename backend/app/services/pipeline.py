from datetime import datetime, timezone, timedelta
from app.core.supabase_client import get_supabase
from app.services.device_auth import authenticate_device
from app.services.anomaly_detection import check_anomaly
from app.services.emission_calculator import calculate_credits_for_facility
from app.services.credit_issuer import issue_credit
from app.services.notification_service import notify_anomaly, notify_credit_issued
from app.config import settings
from app.utils.logger import get_logger
from app.utils.date_utils import parse_iso

logger = get_logger(__name__)


async def run_pipeline(payload: dict):
    """
    Master pipeline orchestrator.
    Called for every MQTT message received from a sensor.

    Expected payload:
    {
        "device_id": "ESP32_FACTORY_001",
        "auth_key": "raw-auth-key-from-device",
        "co2_ppm": 850.5,
        "temperature": 34.2,
        "humidity": 60.1,
        "timestamp": "2025-05-12T14:32:00Z"
    }

    Pipeline steps:
    1. Validate payload fields
    2. Authenticate device (device_id + auth_key)
    3. Store raw reading in Supabase
    4. Run anomaly detection (moving-average-first, returns (anomaly|None, c_avg|None))
    5. Store c_avg in sensor_readings for audit trail
    6. If anomaly → flag reading, insert anomaly_event, notify, STOP
    7. If clean → check if credit calculation window reached
    8. Generate period_id for this window
    9. Calculate credits with quality factor if window reached
    10. Issue credit (IPFS + blockchain + DB)
    11. Send notification
    """
    supabase = get_supabase()

    device_id = payload.get("device_id")
    auth_key = payload.get("auth_key")
    co2_ppm = payload.get("co2_ppm")
    temperature = payload.get("temperature")
    humidity = payload.get("humidity")
    timestamp = payload.get("timestamp")

    # ── Step 1: Validate payload ──────────────────────────────
    if not all([device_id, auth_key, co2_ppm is not None, temperature is not None, humidity is not None, timestamp]):
        logger.error(f"Invalid payload received: {payload}")
        return

    # ── Step 2: Authenticate device ───────────────────────────
    sensor = await authenticate_device(device_id, auth_key)
    if not sensor:
        logger.warning(f"Rejected reading from unauthenticated device: {device_id}")
        return

    sensor_id = sensor["id"]
    facility_id = sensor["facility_id"]
    location_label = sensor.get("location_label", "Unknown")

    # ── Step 3: Store raw reading ─────────────────────────────
    reading_result = supabase.table("sensor_readings").insert({
        "sensor_id": sensor_id,
        "facility_id": facility_id,
        "co2_ppm": co2_ppm,
        "temperature": temperature,
        "humidity": humidity,
        "is_anomaly": False,
        "timestamp": timestamp
    }).execute()

    reading_id = reading_result.data[0]["id"]
    logger.info(f"Reading stored: {reading_id} for facility {facility_id}")

    # ── Step 4: Anomaly detection (returns tuple) ─────────────
    anomaly, c_avg_value = await check_anomaly(sensor_id, facility_id, co2_ppm, reading_id)

    # ── Step 5: Store c_avg in the reading for audit trail ────
    if c_avg_value is not None:
        supabase.table("sensor_readings") \
            .update({"c_avg": c_avg_value}) \
            .eq("id", reading_id) \
            .execute()

    if anomaly:
        # Mark the reading as anomalous
        supabase.table("sensor_readings") \
            .update({
                "is_anomaly": True,
                "anomaly_type": anomaly["anomaly_type"],
                "z_score": anomaly.get("z_score"),
            }) \
            .eq("id", reading_id) \
            .execute()

        # Insert anomaly event with only valid schema columns
        anomaly_result = supabase.table("anomaly_events").insert({
            "sensor_id": anomaly["sensor_id"],
            "facility_id": anomaly["facility_id"],
            "anomaly_type": anomaly["anomaly_type"],
            "z_score": anomaly.get("z_score"),
            "timestamp": timestamp
        }).execute()

        anomaly_id = anomaly_result.data[0]["id"]

        # Notify manager and auditors
        await notify_anomaly(facility_id, anomaly_id, anomaly["anomaly_type"], location_label)

        logger.warning(f"Anomaly pipeline complete for {device_id} — stopping here")
        return  # Do NOT calculate credits for anomalous readings

    # ── Step 7: Credit calculation window check ───────────────
    last_credit = supabase.table("carbon_credits") \
        .select("period_end") \
        .eq("facility_id", facility_id) \
        .order("created_at", desc=True) \
        .limit(1) \
        .execute()

    should_calculate = False
    if not last_credit.data:
        # No credits ever issued — check if we have enough readings
        reading_count = supabase.table("sensor_readings") \
            .select("id", count="exact") \
            .eq("facility_id", facility_id) \
            .eq("is_anomaly", False) \
            .execute()
        should_calculate = (reading_count.count or 0) >= 24
    else:
        last_period_end = parse_iso(last_credit.data[0]["period_end"])
        window = timedelta(hours=settings.CREDIT_CALCULATION_WINDOW_HOURS)
        should_calculate = (datetime.now(timezone.utc) - last_period_end) >= window

    if not should_calculate:
        logger.info(f"Credit calculation window not reached for {facility_id}")
        return

    credit_data = await calculate_credits_for_facility(facility_id)

    if not credit_data:
        logger.info(f"No credit earned for {facility_id} — emissions at or above baseline")
        return

    # ── Step 8: Attach period_id before issuing ───────────────
    # Human-readable period_id (YYYY-MM) for display in app and IPFS report.
    # The blockchain creditId keccak uses reportHash which encodes the exact
    # timestamp internally, so YYYY-MM collisions cannot occur.
    period_end_dt = parse_iso(credit_data["period_end"])
    credit_data["period_id"] = period_end_dt.strftime("%Y-%m")

    # ── Step 9–11: Issue credit (IPFS + Blockchain + DB) ──────
    try:
        credit_record = await issue_credit(credit_data)
    except Exception as e:
        logger.error(f"Credit issuance failed for {facility_id}: {e}")
        return

    # ── Step 12: Notify ───────────────────────────────────────
    await notify_credit_issued(
        facility_id,
        credit_record["id"],
        credit_data["credits_issued"]
    )

    logger.info(
        f"Pipeline complete — {credit_data['credits_issued']} credits "
        f"(raw={credit_data.get('credits_raw')}, q={credit_data.get('quality_factor')}) "
        f"issued for {facility_id}"
    )
