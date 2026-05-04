from pydantic import BaseModel, Field
from typing import Optional


class FacilityDB(BaseModel):
    """Facility record as stored in Supabase."""
    id: str
    name: str
    company_name: str
    location: Optional[str] = None
    industry_type: Optional[str] = None
    baseline_emissions: float
    created_at: Optional[str] = None


class FacilityCreate(BaseModel):
    """Request body for creating a new facility."""
    name: str = Field(..., description="Facility name")
    company_name: str = Field(..., description="Company name")
    location: Optional[str] = Field(None, description="Physical location")
    industry_type: Optional[str] = Field(None, description="Industry type (e.g. 'manufacturing')")
    baseline_emissions: float = Field(
        ...,
        gt=0,
        description="Monthly baseline emissions in tonnes CO₂"
    )
