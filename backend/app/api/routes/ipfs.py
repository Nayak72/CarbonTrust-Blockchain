from fastapi import APIRouter, Depends, HTTPException
from app.core.ipfs_client import ipfs_client
from app.core.security import get_current_user

router = APIRouter(prefix="/ipfs", tags=["ipfs"])


@router.get("/report/{cid}")
async def get_ipfs_report(cid: str, user=Depends(get_current_user)):
    """
    IPFS proxy endpoint — fetches emission report from IPFS by CID.
    Used by the Android auditor to view raw emission reports.
    Proxied through FastAPI to avoid CORS and rate limit issues
    when the mobile app fetches directly from IPFS gateways.
    """
    try:
        report_dict = await ipfs_client.fetch_json(cid)
        
        import json
        # Return the full report including the embedded report_hash
        full_json_str = json.dumps(report_dict, ensure_ascii=False)
        
        return {
            "content": full_json_str,
            "cid": cid,
            "content_type": "application/json"
        }
    except Exception as e:
        raise HTTPException(
            status_code=404,
            detail=f"Report not found on IPFS: {str(e)}"
        )
