from datetime import datetime, timezone
from app.core.supabase_client import get_supabase


async def build_emission_report(credit_data: dict) -> dict:
    """
    Compiles the full emission report JSON that gets uploaded to IPFS.
    This is the document whose hash is stored on the blockchain —
    it serves as the immutable proof of emission reduction.

    The report includes:
    - Facility metadata
    - Emission data (actual vs baseline)
    - Sensor inventory
    - Anomaly count during the period
    - Methodology documentation
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
        "report_version": "1.0",
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "facility": {
            "id": credit_data["facility_id"],
            "name": facility.data["name"],
            "company": facility.data["company_name"],
            "location": facility.data["location"],
            "industry_type": facility.data["industry_type"]
        },
        "period": {
            "start": credit_data["period_start"],
            "end": credit_data["period_end"]
        },
        "emission_data": {
            "actual_emissions_tonnes": credit_data["actual_emissions"],
            "baseline_tonnes": credit_data["baseline_used"],
            "emission_reduction_tonnes": credit_data["emission_reduction"],
            "credits_issued": credit_data["credits_issued"],
            "reading_count": credit_data["reading_count"]
        },
        "sensors": sensors.data,
        "anomaly_events_in_period": anomaly_count.count or 0,
        "methodology": {
            "co2_measurement": "SCD30 NDIR sensor",
            "anomaly_detection": "Z-score (threshold=3.0), frozen value, zero reading",
            "credit_standard": "1 credit = 1 tonne CO2 reduced below baseline",
            "calculation_method": "Trapezoidal integration of excess CO2 ppm"
        }
    }
