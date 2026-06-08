from fastapi import APIRouter, Depends, HTTPException
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user
from app.models.anomaly import AnomalyAcknowledgeRequest
from app.utils.date_utils import iso_now
from app.utils.logger import get_logger

router = APIRouter(prefix="/anomalies", tags=["anomalies"])
logger = get_logger(__name__)


@router.get("/{facility_id}")
async def get_anomalies(
    facility_id: str,
    limit: int = 50,
    unacknowledged_only: bool = False,
    user=Depends(get_current_user)
):
    """
    Fetches anomaly events for a facility.
    Supports filtering for unacknowledged anomalies only.
    """
    supabase = get_supabase()
    query = supabase.table("anomaly_events") \
        .select("*") \
        .eq("facility_id", facility_id) \
        .order("timestamp", desc=True) \
        .limit(limit)

    if unacknowledged_only:
        query = query.eq("is_acknowledged", False)

    result = query.execute()
    return {"anomalies": result.data, "count": len(result.data)}


@router.put("/{anomaly_id}/acknowledge")
async def acknowledge_anomaly(
    anomaly_id: str,
    body: AnomalyAcknowledgeRequest,
    user=Depends(get_current_user)
):
    """
    Acknowledges an anomaly event.
    Records who acknowledged it, when, and an optional note.
    """
    supabase = get_supabase()

    result = supabase.table("anomaly_events") \
        .update({
            "is_acknowledged": True,
            "acknowledged_by": user["user_id"],
            "acknowledged_at": iso_now(),
            "acknowledgement_note": body.note
        }) \
        .eq("id", anomaly_id) \
        .execute()

    if not result.data:
        raise HTTPException(status_code=404, detail="Anomaly not found")

    logger.info(f"Anomaly {anomaly_id} acknowledged by {user['user_id']}")
    return {"status": "acknowledged", "anomaly": result.data[0]}
