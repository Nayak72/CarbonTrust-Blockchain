from pydantic import BaseModel
from typing import Optional


class CarbonCreditDB(BaseModel):
    """Carbon credit record as stored in Supabase."""
    id: str
    facility_id: str
    credits_issued: float
    emission_reduction: float
    actual_emissions: float
    baseline_used: float
    period_start: str
    period_end: str
    ipfs_cid: str
    tx_hash: str
    block_number: Optional[int] = None
    status: str = "verified"
    created_at: Optional[str] = None


class CreditCalculationResult(BaseModel):
    """Result of a credit calculation for a facility."""
    facility_id: str
    credits_issued: float
    emission_reduction: float
    actual_emissions: float
    baseline_used: float
    period_start: str
    period_end: str
    reading_count: int


class CreditIssuanceResponse(BaseModel):
    """Response after a credit is issued (IPFS + blockchain + DB)."""
    status: str
    credit: Optional[CarbonCreditDB] = None
    message: Optional[str] = None
