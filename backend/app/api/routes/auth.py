from fastapi import APIRouter, Depends
from pydantic import BaseModel
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user
from app.utils.logger import get_logger

router = APIRouter(prefix="/auth", tags=["auth"])
logger = get_logger(__name__)


class FCMTokenRequest(BaseModel):
    fcm_token: str


@router.post("/fcm-token")
async def register_fcm_token(
    body: FCMTokenRequest,
    user=Depends(get_current_user)
):
    """
    Called by Android app on login and when FCM token refreshes.
    Stores the FCM token in user_profiles for push notification delivery.
    """
    supabase = get_supabase()
    supabase.table("user_profiles") \
        .update({"fcm_token": body.fcm_token}) \
        .eq("id", user["user_id"]) \
        .execute()
    logger.info(f"FCM token registered for user {user['user_id']}")
    return {"status": "ok", "message": "FCM token registered"}
