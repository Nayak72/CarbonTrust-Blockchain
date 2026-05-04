from datetime import datetime, timezone, timedelta
from app.core.supabase_client import get_supabase
from app.services.device_auth import authenticate_device
from app.services.anomaly_detection import check_anomaly
from app.services.emission_calculator import calculate_credits_for_facility
from app.services.credit_issuer import issue_credit
from app.services.notification_service import notify_anomaly, notify_credit_issued
from app.config import settings
from app.utils.logger import get_logger

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
    4. Run anomaly detection
    5. If anomaly → flag, notify, STOP
    6. If clean → check if credit calculation window reached
    7. Calculate credits if window reached
    8. Issue credit (IPFS + blockchain + DB)
    9. Send notification
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

    # ── Step 4: Anomaly detection ─────────────────────────────
    anomaly = await check_anomaly(sensor_id, facility_id, co2_ppm, reading_id)

    if anomaly:
        # Mark the reading as anomalous
        supabase.table("sensor_readings") \
            .update({
                "is_anomaly": True,
                "anomaly_type": anomaly["anomaly_type"],
                "z_score": anomaly.get("z_score")
            }) \
            .eq("id", reading_id) \
            .execute()

        # Insert anomaly event
        anomaly_result = supabase.table("anomaly_events").insert({
            **anomaly,
            "timestamp": timestamp
        }).execute()

        anomaly_id = anomaly_result.data[0]["id"]

        # Notify manager and auditors
        await notify_anomaly(facility_id, anomaly_id, anomaly["anomaly_type"], location_label)

        logger.warning(f"Anomaly pipeline complete for {device_id} — stopping here")
        return  # Do NOT calculate credits for anomalous readings

    # ── Step 5–8: Credit calculation (runs periodically) ──────
    # Only attempt credit calculation when the window has elapsed
    # since the last credit issuance to avoid DB overload
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
        last_period_end = datetime.fromisoformat(last_credit.data[0]["period_end"])
        window = timedelta(hours=settings.CREDIT_CALCULATION_WINDOW_HOURS)
        should_calculate = (datetime.now(timezone.utc) - last_period_end) >= window

    if not should_calculate:
        logger.info(f"Credit calculation window not reached for {facility_id}")
        return

    credit_data = await calculate_credits_for_facility(facility_id)

    if not credit_data:
        logger.info(f"No credit earned for {facility_id} — emissions at or above baseline")
        return

    # ── Step 9–12: Issue credit (IPFS + Blockchain + DB) ──────
    try:
        credit_record = await issue_credit(credit_data)
    except Exception as e:
        logger.error(f"Credit issuance failed for {facility_id}: {e}")
        return

    # ── Step 13: Notify ───────────────────────────────────────
    await notify_credit_issued(
        facility_id,
        credit_record["id"],
        credit_data["credits_issued"]
    )

    logger.info(
        f"Pipeline complete — {credit_data['credits_issued']} credits "
        f"issued for {facility_id}"
    )
