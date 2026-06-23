from fastapi import APIRouter, Depends, HTTPException, Request
from app.core.security import require_manager
from app.services.emission_calculator import calculate_credits_for_facility
from app.services.credit_issuer import issue_credit
from app.utils.logger import get_logger

from slowapi import Limiter
from slowapi.util import get_remote_address
limiter = Limiter(key_func=get_remote_address)

router = APIRouter(prefix="/credits", tags=["credits"])
logger = get_logger(__name__)


@router.post("/recalculate/{facility_id}")
@limiter.limit("1/minute")
async def manual_recalculate(request: Request, facility_id: str, user=Depends(require_manager)):
    """
    Manually triggers credit recalculation for a facility.
    Only accessible to managers of that specific facility.

    This is used when the manager wants to force a credit check
    outside the automatic pipeline window.
    """
    if user["facility_id"] != facility_id:
        raise HTTPException(status_code=403, detail="Not your facility")

    logger.info(f"Manual recalculation triggered for facility {facility_id}")

    credit_data = await calculate_credits_for_facility(facility_id)
    if not credit_data:
        return {
            "status": "no_credit",
            "message": "No emission reduction achieved in current window"
        }

    try:
        credit_record = await issue_credit(credit_data)
        return {"status": "issued", "credit": credit_record}
    except Exception as e:
        logger.error(f"Manual credit issuance failed: {e}")
        raise HTTPException(
            status_code=500,
            detail="Credit issuance failed due to an internal error."
        )
