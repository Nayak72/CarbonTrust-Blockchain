from pydantic import BaseModel, Field
from typing import Optional


class AnomalyEvent(BaseModel):
    """Anomaly event detected during sensor reading processing."""
    sensor_id: str
    facility_id: str
    reading_id: str
    anomaly_type: str = Field(
        ...,
        description="One of: zscore_breach, frozen_value, zero_reading, connectivity_gap"
    )
    co2_value: float
    z_score: Optional[float] = None


class AnomalyEventDB(BaseModel):
    """Anomaly event as stored in Supabase."""
    id: str
    sensor_id: str
    facility_id: str
    reading_id: Optional[str] = None
    anomaly_type: str
    co2_value: float
    z_score: Optional[float] = None
    is_acknowledged: bool = False
    acknowledged_by: Optional[str] = None
    acknowledged_at: Optional[str] = None
    acknowledgement_note: Optional[str] = None
    timestamp: str
    created_at: Optional[str] = None


class AnomalyAcknowledgeRequest(BaseModel):
    """Request body for acknowledging an anomaly."""
    note: Optional[str] = Field(None, description="Optional acknowledgement note")
