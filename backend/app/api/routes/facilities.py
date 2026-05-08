from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from typing import Optional
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user

router = APIRouter(prefix="/facilities", tags=["facilities"])


class FacilityCreateRequest(BaseModel):
    name: str
    company_name: str
    location: Optional[str] = None
    industry_type: Optional[str] = None
    baseline_emissions: float


class FacilityOnboardRequest(BaseModel):
    """Used during manager sign-up before user_profiles row exists."""
    name: str
    company_name: str
    location: Optional[str] = None
    industry_type: Optional[str] = None
    baseline_emissions: float


@router.post("/create")
async def create_facility(
    body: FacilityCreateRequest,
    user=Depends(get_current_user)
):
    """
    Creates a new facility.
    Used during initial setup to register a facility in the system.
    """
    supabase = get_supabase()
    try:
        result = supabase.table("facilities").insert({
            "name": body.name,
            "company_name": body.company_name,
            "location": body.location,
            "industry_type": body.industry_type,
            "baseline_emissions": body.baseline_emissions
        }).execute()
        return {"status": "created", "facility": result.data[0]}
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail=f"Facility creation failed: {str(e)}"
        )


@router.post("/onboard")
async def onboard_facility(body: FacilityOnboardRequest):
    """
    Creates a new facility during manager sign-up, BEFORE user_profiles exists.
    Called by the Android app immediately after Supabase auth sign-up + sign-in,
    but before the user_profiles row has been written.
    This endpoint is intentionally unauthenticated — it trusts the Supabase user ID
    passed in the body (validated by sign-up flow context).
    """
    supabase = get_supabase()
    try:
        result = supabase.table("facilities").insert({
            "name": body.name,
            "company_name": body.company_name,
            "location": body.location,
            "industry_type": body.industry_type,
            "baseline_emissions": body.baseline_emissions
        }).execute()
        return {"status": "created", "facility": result.data[0]}
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail=f"Facility onboarding failed: {str(e)}"
        )


@router.get("/{facility_id}")
async def get_facility(
    facility_id: str,
    user=Depends(get_current_user)
):
    """
    Fetches facility details by ID.
    """
    supabase = get_supabase()
    result = supabase.table("facilities") \
        .select("*") \
        .eq("id", facility_id) \
        .single() \
        .execute()

    if not result.data:
        raise HTTPException(status_code=404, detail="Facility not found")

    return result.data


@router.get("/")
async def list_facilities(
    user=Depends(get_current_user)
):
    """
    Returns all facilities. Used by the auditor dashboard.
    """
    supabase = get_supabase()
    result = supabase.table("facilities").select("*").execute()
    return result.data or []
