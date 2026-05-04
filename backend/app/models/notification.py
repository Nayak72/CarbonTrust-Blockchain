from pydantic import BaseModel, Field
from typing import Optional


class NotificationDB(BaseModel):
    """Notification record as stored in Supabase."""
    id: str
    user_id: str
    title: str
    body: str
    type: str = Field(..., description="One of: anomaly, credit_issued, system")
    related_id: Optional[str] = None
    is_read: bool = False
    created_at: Optional[str] = None


class NotificationCreate(BaseModel):
    """Internal model for creating a notification."""
    user_id: str
    title: str
    body: str
    type: str
    related_id: Optional[str] = None
