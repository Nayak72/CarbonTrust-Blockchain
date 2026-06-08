from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user
from app.utils.logger import get_logger

router = APIRouter(prefix="/assignments", tags=["assignments"])
logger = get_logger(__name__)


class AssignRequest(BaseModel):
    auditor_id: str
    facility_id: str


class RevokeRequest(BaseModel):
    auditor_id: str
    facility_id: str


# ──────────────────────────────────────────────────────────────────
# POST /assignments/assign
# ──────────────────────────────────────────────────────────────────

@router.post("/assign")
async def assign_auditor(body: AssignRequest, user=Depends(get_current_user)):
    """
    Assign an auditor to a facility.
    - Auth: MANAGER only.
    - Manager can only assign auditors to their own facility.
    - Creates or reactivates a row in auditor_assignments.
    """
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can assign auditors")

    if user.get("facility_id") != body.facility_id:
        raise HTTPException(status_code=403, detail="You can only assign auditors to your own facility")

    supabase = get_supabase()

    # Verify the target user is actually an AUDITOR
    auditor = supabase.table("user_profiles") \
        .select("id, role, full_name") \
        .eq("id", body.auditor_id) \
        .single() \
        .execute()

    if not auditor.data:
        raise HTTPException(status_code=404, detail="Auditor not found")

    if auditor.data.get("role") != "AUDITOR":
        raise HTTPException(status_code=400, detail="Target user is not an auditor")

    # Upsert: if the record exists (even inactive), reactivate it
    existing = supabase.table("auditor_assignments") \
        .select("id, is_active") \
        .eq("auditor_id", body.auditor_id) \
        .eq("facility_id", body.facility_id) \
        .execute()

    if existing.data:
        # Reactivate if previously revoked
        supabase.table("auditor_assignments") \
            .update({"is_active": True, "assigned_by": user["user_id"]}) \
            .eq("auditor_id", body.auditor_id) \
            .eq("facility_id", body.facility_id) \
            .execute()
        return {"status": "reactivated", "auditor_id": body.auditor_id, "facility_id": body.facility_id}

    result = supabase.table("auditor_assignments").insert({
        "auditor_id": body.auditor_id,
        "facility_id": body.facility_id,
        "assigned_by": user["user_id"],
        "is_active": True,
    }).execute()

    return {"status": "assigned", "assignment": result.data[0]}


# ──────────────────────────────────────────────────────────────────
# DELETE /assignments/revoke
# ──────────────────────────────────────────────────────────────────

@router.delete("/revoke")
async def revoke_auditor(body: RevokeRequest, user=Depends(get_current_user)):
    """
    Revoke an auditor's access to a facility.
    - Auth: MANAGER of that facility.
    - Sets is_active = False (soft delete, keeps audit trail).
    """
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can revoke auditor assignments")

    if user.get("facility_id") != body.facility_id:
        raise HTTPException(status_code=403, detail="You can only revoke auditors from your own facility")

    supabase = get_supabase()

    result = supabase.table("auditor_assignments") \
        .update({"is_active": False}) \
        .eq("auditor_id", body.auditor_id) \
        .eq("facility_id", body.facility_id) \
        .execute()

    if not result.data:
        raise HTTPException(status_code=404, detail="Assignment not found")

    return {"status": "revoked", "auditor_id": body.auditor_id, "facility_id": body.facility_id}


# ──────────────────────────────────────────────────────────────────
# GET /assignments/my-facilities
# ──────────────────────────────────────────────────────────────────

@router.get("/my-facilities")
async def get_my_assigned_facilities(user=Depends(get_current_user)):
    """
    Returns the list of facilities the currently authenticated auditor is assigned to.
    - Auth: AUDITOR only.
    """
    if user.get("role") != "AUDITOR":
        raise HTTPException(status_code=403, detail="Only auditors can use this endpoint")

    supabase = get_supabase()

    assignments = supabase.table("auditor_assignments") \
        .select("facility_id") \
        .eq("auditor_id", user["user_id"]) \
        .eq("is_active", True) \
        .execute()

    facility_ids = [a["facility_id"] for a in (assignments.data or [])]
    if not facility_ids:
        return []

    facilities = supabase.table("facilities") \
        .select("*") \
        .in_("id", facility_ids) \
        .execute()

    return facilities.data or []


# ──────────────────────────────────────────────────────────────────
# GET /assignments/facility/{facility_id}/auditors
# ──────────────────────────────────────────────────────────────────

@router.get("/facility/{facility_id}/auditors")
async def get_facility_auditors(facility_id: str, user=Depends(get_current_user)):
    """
    Returns all auditors currently assigned to a facility.
    - Auth: MANAGER of that facility.
    """
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can view facility auditors")

    if user.get("facility_id") != facility_id:
        raise HTTPException(status_code=403, detail="You can only view auditors for your own facility")

    supabase = get_supabase()

    assignments = supabase.table("auditor_assignments") \
        .select("id, auditor_id, assigned_at, assigned_by, is_active") \
        .eq("facility_id", facility_id) \
        .eq("is_active", True) \
        .execute()

    if not assignments.data:
        return []

    auditor_ids = [a["auditor_id"] for a in assignments.data]
    profiles = supabase.table("user_profiles") \
        .select("id, full_name, email") \
        .in_("id", auditor_ids) \
        .execute()

    # Merge assignment metadata with profile data
    profile_map = {p["id"]: p for p in (profiles.data or [])}
    return [
        {
            **profile_map.get(a["auditor_id"], {}),
            "assignment_id": a["id"],
            "assigned_at": a["assigned_at"],
            "assigned_by": a["assigned_by"],
        }
        for a in assignments.data
    ]


# ──────────────────────────────────────────────────────────────────
# GET /assignments/available-auditors
# ──────────────────────────────────────────────────────────────────

@router.get("/available-auditors")
async def get_available_auditors(user=Depends(get_current_user)):
    """
    Returns all users with role=AUDITOR (for the assignment picker UI).
    - Auth: MANAGER only.
    """
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can view the auditor list")

    supabase = get_supabase()

    result = supabase.table("user_profiles") \
        .select("id, full_name, email, created_at") \
        .eq("role", "AUDITOR") \
        .order("full_name") \
        .execute()

    return result.data or []
