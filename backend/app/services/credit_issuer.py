from app.core.supabase_client import get_supabase
from app.core.ipfs_client import ipfs_client
from app.core.web3_client import web3_client
from app.services.report_builder import build_emission_report
from app.utils.logger import get_logger

logger = get_logger(__name__)


async def issue_credit(credit_data: dict) -> dict:
    """
    Orchestrates the full credit issuance pipeline:
    1. Build emission report JSON
    2. Upload to IPFS → get CID
    3. Record on Polygon blockchain → get TX hash
    4. Store in Supabase carbon_credits table

    Returns the complete credit record from Supabase.
    """
    supabase = get_supabase()

    # Step 1: Build report
    logger.info(f"Building emission report for facility: {credit_data['facility_id']}")
    report = await build_emission_report(credit_data)

    # Step 2: Upload to IPFS
    logger.info("Uploading report to IPFS via Pinata...")
    ipfs_cid = await ipfs_client.upload_json(
        report,
        name=f"emission_report_{credit_data['facility_id']}_{credit_data['period_end']}"
    )
    logger.info(f"IPFS CID: {ipfs_cid}")

    # Step 3: Record on blockchain
    logger.info("Recording credit on Polygon blockchain...")
    credits_grams = int(credit_data["credits_issued"] * 1_000_000)  # Convert tonnes to grams
    blockchain_result = await web3_client.issue_credit_on_chain(
        facility_id=credit_data["facility_id"],
        credits_kg=credits_grams,
        ipfs_cid=ipfs_cid
    )
    logger.info(f"TX Hash: {blockchain_result['tx_hash']}")

    # Step 4: Store in Supabase
    credit_record = {
        "facility_id": credit_data["facility_id"],
        "credits_issued": credit_data["credits_issued"],
        "emission_reduction": credit_data["emission_reduction"],
        "actual_emissions": credit_data["actual_emissions"],
        "baseline_used": credit_data["baseline_used"],
        "period_start": credit_data["period_start"],
        "period_end": credit_data["period_end"],
        "ipfs_cid": ipfs_cid,
        "tx_hash": blockchain_result["tx_hash"],
        "block_number": blockchain_result["block_number"],
        "status": "verified"
    }

    result = supabase.table("carbon_credits").insert(credit_record).execute()
    inserted = result.data[0]
    logger.info(f"Credit stored in Supabase: {inserted['id']}")

    return inserted
