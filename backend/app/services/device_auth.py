import hashlib
from app.core.supabase_client import get_supabase
from app.utils.logger import get_logger

logger = get_logger(__name__)


async def authenticate_device(device_id: str, auth_key: str) -> dict | None:
    """
    Validates that the device_id exists and the auth_key matches.
    Returns sensor record if valid, None if invalid.

    The auth_key stored in Supabase is a SHA-256 hash of the raw key.
    The ESP32 device sends the raw key, which we hash here for comparison.
    """
    supabase = get_supabase()
    result = supabase.table("sensors") \
        .select("id, facility_id, location_label, is_active, auth_key") \
        .eq("device_id", device_id) \
        .eq("is_active", True) \
        .single() \
        .execute()

    if not result.data:
        logger.warning(f"Device not found or inactive: {device_id}")
        return None

    sensor = result.data
    stored_hash = sensor.get("auth_key")
    incoming_hash = hashlib.sha256(auth_key.encode()).hexdigest()

    if stored_hash != incoming_hash:
        logger.warning(f"Auth key mismatch for device: {device_id}")
        return None

    # Update last_seen timestamp
    supabase.table("sensors") \
        .update({"last_seen": "now()"}) \
        .eq("device_id", device_id) \
        .execute()

    logger.info(f"Device authenticated: {device_id}")
    return {
        "id": sensor["id"],
        "facility_id": sensor["facility_id"],
        "location_label": sensor.get("location_label")
    }
