from pydantic import BaseModel, Field
from typing import Optional


class UserProfile(BaseModel):
    """User profile as stored in Supabase."""
    id: str
    full_name: str
    email: str
    role: str = Field(..., description="One of: MANAGER, AUDITOR")
    facility_id: Optional[str] = None
    fcm_token: Optional[str] = None
    avatar_url: Optional[str] = None
    created_at: Optional[str] = None


class FCMTokenUpdate(BaseModel):
    """Request body for updating FCM token."""
    fcm_token: str = Field(..., description="Firebase Cloud Messaging token")
