from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user
import hashlib

router = APIRouter(prefix="/sensors", tags=["sensors"])


class SensorRegistrationRequest(BaseModel):
    device_id: str
    auth_key: str
    location_label: str
    facility_id: str


@router.post("/register")
async def register_sensor(
    body: SensorRegistrationRequest,
    user=Depends(get_current_user)
):
    """
    Registers a new ESP32 sensor device.
    Auth key is hashed with SHA-256 before storage so the raw key
    is never persisted — only the device knows the raw key.
    """
    supabase = get_supabase()
    hashed_key = hashlib.sha256(body.auth_key.encode()).hexdigest()

    try:
        insert_data = {
            "device_id": body.device_id,
            "auth_key": hashed_key,
            "location_label": body.location_label,
            "facility_id": body.facility_id
        }

        # For virtual sensors, also store the raw key for simulator restart
        if body.device_id.startswith("SIM_"):
            insert_data["sim_auth_key_raw"] = body.auth_key

        result = supabase.table("sensors").insert(insert_data).execute()

        return {"status": "registered", "sensor": result.data[0]}
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail=f"Sensor registration failed: {str(e)}"
        )
