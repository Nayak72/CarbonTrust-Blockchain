import json
import hashlib
from app.core.supabase_client import get_supabase
from app.core.ipfs_client import ipfs_client
from app.core.web3_client import web3_client
from app.services.report_builder import build_emission_report
from app.utils.logger import get_logger

logger = get_logger(__name__)


async def issue_credit(credit_data: dict) -> dict:
    """
    Orchestrates the full credit issuance pipeline following the analytical model:
    1. Build emission report JSON (version 2.0)
    2. Compute SHA-256 report_hash of the canonical report
    3. Embed report_hash inside the report itself and re-serialize
    4. Upload final report to IPFS → get CID
    5. Record on Polygon blockchain (facilityId, periodId, creditsAdj,
       totalEmissions, emissionReduction, reportHash, ipfsCid) → get TX hash
    6. Store complete record in Supabase carbon_credits table

    Returns the complete credit record from Supabase.
    """
    supabase = get_supabase()

    # Step 1: Build report (report_hash is None at this stage)
    logger.info(f"Building emission report for facility: {credit_data['facility_id']}")
    report = await build_emission_report(credit_data)

    # Step 2: Compute SHA-256 hash of the canonical report (without the hash field)
    report_json_str = json.dumps(report, sort_keys=True, ensure_ascii=False)
    report_hash = hashlib.sha256(report_json_str.encode()).hexdigest()

    # Step 3: Embed the hash into the report so the final IPFS document is self-describing
    report["report_hash"] = report_hash

    # Step 4: Upload the hash-embedded report to IPFS
    logger.info("Uploading report to IPFS via Pinata...")
    ipfs_cid = await ipfs_client.upload_json(
        report,
        name=f"emission_report_{credit_data['facility_id']}_{credit_data.get('period_id', credit_data['period_end'])}"
    )
    logger.info(f"IPFS CID: {ipfs_cid}")

    # Step 5: Record on blockchain
    logger.info("Recording credit on Polygon blockchain...")
    credits_grams = int(credit_data["credits_issued"] * 1_000_000)   # tonnes → grams ×1e6
    total_emissions_grams = int(credit_data["actual_emissions"] * 1_000_000)
    emission_reduction_grams = int(credit_data["emission_reduction"] * 1_000_000)
    report_hash_bytes32 = web3_client.hex_to_bytes32(report_hash)
    period_id = credit_data.get("period_id", "")

    blockchain_result = await web3_client.issue_credit_on_chain(
        facility_id=credit_data["facility_id"],
        period_id=period_id,
        credits_adj=credits_grams,
        total_emissions_grams=total_emissions_grams,
        emission_reduction_grams=emission_reduction_grams,
        report_hash_bytes32=report_hash_bytes32,
        ipfs_cid=ipfs_cid,
    )
    logger.info(f"TX Hash: {blockchain_result['tx_hash']}")

    # Step 6: Store in Supabase
    credit_record = {
        "facility_id": credit_data["facility_id"],
        "credits_issued": credit_data["credits_issued"],            # Credits_adj
        "credits_raw": credit_data.get("credits_raw"),
        "quality_factor": credit_data.get("quality_factor"),
        "emission_reduction": credit_data["emission_reduction"],
        "actual_emissions": credit_data["actual_emissions"],
        "baseline_used": credit_data["baseline_used"],
        "period_start": credit_data["period_start"],
        "period_end": credit_data["period_end"],
        "period_id": period_id,
        "total_emissions_kg": round(credit_data["actual_emissions"] * 1000, 4),
        "emission_reduction_kg": round(credit_data["emission_reduction"] * 1000, 4),
        "anomaly_count": credit_data.get("anomaly_count", 0),
        "report_hash": report_hash,
        "ipfs_cid": ipfs_cid,
        "tx_hash": blockchain_result["tx_hash"],
        "block_number": blockchain_result["block_number"],
        "status": "verified",
    }

    result = supabase.table("carbon_credits").insert(credit_record).execute()
    inserted = result.data[0]
    logger.info(f"Credit stored in Supabase: {inserted['id']}")

    return inserted
