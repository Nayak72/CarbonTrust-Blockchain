from fastapi import APIRouter, Depends, HTTPException
from typing import List, Dict, Any
from app.core.supabase_client import get_supabase
from app.core.security import require_admin
from app.utils.logger import get_logger

router = APIRouter(prefix="/admin", tags=["admin"])
logger = get_logger(__name__)

@router.get("/dashboard")
async def get_admin_dashboard(user=Depends(require_admin)):
    """
    Returns aggregated data for the admin dashboard:
    Companies -> Facilities -> Assigned Auditors
    """
    supabase = get_supabase()

    # 1. Fetch all facilities
    facilities_res = supabase.table("facilities").select("*").execute()
    facilities = facilities_res.data or []

    # 2. Fetch all active assignments
    assignments_res = supabase.table("auditor_assignments") \
        .select("id, facility_id, auditor_id, assigned_at") \
        .eq("is_active", True) \
        .execute()
    assignments = assignments_res.data or []

    # 3. Fetch user profiles for auditors
    auditor_ids = list(set([a["auditor_id"] for a in assignments]))
    profiles = []
    if auditor_ids:
        profiles_res = supabase.table("user_profiles") \
            .select("id, full_name, email") \
            .in_("id", auditor_ids) \
            .execute()
        profiles = profiles_res.data or []

    profile_map = {p["id"]: p for p in profiles}

    # 4. Group data
    # Create a map of facility_id -> list of auditor dicts
    facility_auditors = {}
    for a in assignments:
        fid = a["facility_id"]
        auditor = profile_map.get(a["auditor_id"])
        if auditor:
            if fid not in facility_auditors:
                facility_auditors[fid] = []
            facility_auditors[fid].append({
                "id": auditor["id"],
                "full_name": auditor["full_name"],
                "email": auditor["email"]
            })

    # Group facilities by company_name
    companies_map: Dict[str, Dict[str, Any]] = {}
    for fac in facilities:
        c_name = fac.get("company_name", "Unknown Company")
        if c_name not in companies_map:
            companies_map[c_name] = {
                "company_name": c_name,
                "facilities": []
            }
        
        fac_data = {
            "id": fac["id"],
            "name": fac["name"],
            "location": fac.get("location"),
            "industry_type": fac.get("industry_type"),
            "baseline_emissions": fac.get("baseline_emissions"),
            "auditors": facility_auditors.get(fac["id"], [])
        }
        companies_map[c_name]["facilities"].append(fac_data)

    # Convert to list and sort
    dashboard_data = list(companies_map.values())
    dashboard_data.sort(key=lambda x: x["company_name"])

    return dashboard_data
