from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from app.core.security import get_current_user
from app.services.pipeline import run_pipeline
from app.utils.logger import get_logger

router = APIRouter(prefix="/readings", tags=["readings"])
logger = get_logger(__name__)


class ManualReadingRequest(BaseModel):
    device_id: str
    auth_key: str
    co2_ppm: float
    temperature: float
    humidity: float
    timestamp: str


@router.post("/submit")
async def submit_reading(
    body: ManualReadingRequest,
    user=Depends(get_current_user)
):
    """
    Manual reading submission endpoint — allows submitting a sensor
    reading via REST API instead of MQTT. Useful for testing and
    for devices that can't use MQTT directly.

    Runs the same pipeline as MQTT-ingested readings.
    """
    logger.info(f"Manual reading submission for device {body.device_id}")

    try:
        await run_pipeline(body.model_dump())
        return {"status": "ok", "message": "Reading submitted and processed"}
    except Exception as e:
        logger.error(f"Manual reading processing failed: {e}")
        raise HTTPException(
            status_code=500,
            detail=f"Processing failed: {str(e)}"
        )
