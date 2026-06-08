from datetime import datetime, timezone
from app.core.supabase_client import get_supabase
from app.config import settings


async def build_emission_report(credit_data: dict) -> dict:
    """
    Compiles the full emission report JSON (version 2.0) that gets uploaded to IPFS.
    This is the document whose SHA-256 hash is stored on the blockchain —
    it serves as the immutable proof of emission reduction.

    The report includes:
    - Facility metadata
    - Period identification
    - Emission data (actual vs baseline, raw credits, quality factor, Credits_adj)
    - Methodology documentation (all analytical model parameters)
    - Sensor inventory
    - Anomaly event count during period
    - report_hash placeholder (filled in by credit_issuer.py after generation)
    """
    supabase = get_supabase()

    # Fetch facility details
    facility = supabase.table("facilities") \
        .select("name, company_name, location, industry_type") \
        .eq("id", credit_data["facility_id"]) \
        .single() \
        .execute()

    # Fetch sensors belonging to this facility
    sensors = supabase.table("sensors") \
        .select("device_id, location_label") \
        .eq("facility_id", credit_data["facility_id"]) \
        .execute()

    # Count anomaly events during the credit period
    anomaly_count = supabase.table("anomaly_events") \
        .select("id", count="exact") \
        .eq("facility_id", credit_data["facility_id"]) \
        .gte("timestamp", credit_data["period_start"]) \
        .lte("timestamp", credit_data["period_end"]) \
        .execute()

    return {
        "report_version": "2.0",
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "facility": {
            "id": credit_data["facility_id"],
            "name": facility.data["name"],
            "company": facility.data["company_name"],
            "location": facility.data["location"],
            "industry_type": facility.data["industry_type"],
        },
        "period": {
            "start": credit_data["period_start"],
            "end": credit_data["period_end"],
            "period_id": credit_data.get("period_id", ""),
        },
        "emission_data": {
            "actual_emissions_kg": round(credit_data["actual_emissions"] * 1000, 2),
            "actual_emissions_tonnes": credit_data["actual_emissions"],
            "baseline_kg": round(credit_data["baseline_used"] * 1000, 2),
            "baseline_tonnes": credit_data["baseline_used"],
            "emission_reduction_kg": round(credit_data["emission_reduction"] * 1000, 2),
            "emission_reduction_tonnes": credit_data["emission_reduction"],
            "credits_raw": credit_data.get("credits_raw"),
            "quality_factor": credit_data.get("quality_factor"),
            "credits_adj": credit_data["credits_issued"],      # Final Credits_adj
            "reading_count": credit_data["reading_count"],
            "anomaly_count": credit_data.get("anomaly_count", 0),
        },
        "methodology": {
            "moving_average_window": settings.MOVING_AVERAGE_WINDOW,
            "anomaly_zscore_threshold": settings.ANOMALY_ZSCORE_THRESHOLD,
            "ambient_co2_ppm": settings.AMBIENT_CO2_PPM,
            "flow_rate_m3_s": settings.DEFAULT_FLOW_RATE_M3_S,
            "alpha_calibration": settings.EMISSION_ALPHA,
            "beta_kg_per_credit": settings.CREDIT_BETA_KG,
            "credit_standard": "1 credit = 1 tonne CO2 reduced below baseline",
            "calculation_method": "Trapezoidal integration with moving-average smoothing",
        },
        "sensors": sensors.data,
        "anomaly_events_in_period": anomaly_count.count or 0,
        "report_hash": None,   # Filled in by credit_issuer.py after SHA-256 computation
    }
