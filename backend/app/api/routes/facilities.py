from fastapi import APIRouter, Depends, HTTPException, Request
from pydantic import BaseModel
from typing import Optional
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user

from slowapi import Limiter
from slowapi.util import get_remote_address
limiter = Limiter(key_func=get_remote_address)

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
            detail="Facility creation failed due to an internal error."
        )


@router.post("/onboard")
@limiter.limit("3/day")
async def onboard_facility(request: Request, body: FacilityOnboardRequest):
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
            detail="Facility onboarding failed due to an internal error."
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
async def list_facilities(user=Depends(get_current_user)):
    """
    Returns facilities scoped to the user's role:
      - AUDITOR  → only facilities assigned to them via auditor_assignments
      - MANAGER  → only their own facility (facility_id from user profile)
      - Other    → all facilities (admin / service accounts)
    """
    supabase = get_supabase()
    role = user.get("role")

    if role == "AUDITOR":
        # Fetch only facilities the auditor is currently assigned to
        assignments = supabase.table("auditor_assignments") \
            .select("facility_id") \
            .eq("auditor_id", user["user_id"]) \
            .eq("is_active", True) \
            .execute()

        facility_ids = [a["facility_id"] for a in (assignments.data or [])]
        if not facility_ids:
            return []

        result = supabase.table("facilities") \
            .select("*") \
            .in_("id", facility_ids) \
            .execute()

    elif role == "MANAGER":
        # Manager can only see their own facility
        result = supabase.table("facilities") \
            .select("*") \
            .eq("id", user.get("facility_id")) \
            .execute()

    else:
        result = supabase.table("facilities").select("*").execute()

    return result.data or []
