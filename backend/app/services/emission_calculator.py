from datetime import datetime, timezone, timedelta
from app.core.supabase_client import get_supabase
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)


async def calculate_credits_for_facility(facility_id: str) -> dict | None:
    """
    Calculates carbon credits for the last CREDIT_CALCULATION_WINDOW_HOURS
    for a given facility.

    Steps:
    1. Fetch all clean (non-anomaly) readings for the window
    2. Calculate total CO₂ emitted (kg) using trapezoidal integration
    3. Compare vs prorated baseline
    4. If reduction achieved → return credit amount in tonnes
    5. If over baseline → return None (no credits)

    CO₂ mass calculation:
        mass (kg) = excess_fraction × flow_rate (m³/s) × duration (s) × CO₂_density (kg/m³)
        where excess_fraction = (avg_ppm - ambient_ppm) / 1,000,000
    """
    supabase = get_supabase()
    window_hours = settings.CREDIT_CALCULATION_WINDOW_HOURS
    period_end = datetime.now(timezone.utc)
    period_start = period_end - timedelta(hours=window_hours)

    # Fetch facility baseline
    facility = supabase.table("facilities") \
        .select("baseline_emissions") \
        .eq("id", facility_id) \
        .single() \
        .execute()

    if not facility.data:
        logger.error(f"Facility not found: {facility_id}")
        return None

    # baseline_emissions is in tonnes/month — prorate to window
    monthly_baseline_tonnes = facility.data["baseline_emissions"]
    hours_in_month = 730  # Average hours per month
    window_baseline_tonnes = monthly_baseline_tonnes * (window_hours / hours_in_month)

    # Fetch clean readings for the window
    readings = supabase.table("sensor_readings") \
        .select("co2_ppm, timestamp") \
        .eq("facility_id", facility_id) \
        .eq("is_anomaly", False) \
        .gte("timestamp", period_start.isoformat()) \
        .lte("timestamp", period_end.isoformat()) \
        .order("timestamp") \
        .execute()

    if not readings.data or len(readings.data) < 2:
        logger.info(f"Not enough readings for credit calculation: {facility_id}")
        return None

    # Calculate total CO₂ emitted using trapezoidal integration
    # CO₂ density at STP ≈ 1.96 kg/m³
    CO2_DENSITY_KG_M3 = 1.96
    flow_rate = settings.DEFAULT_FLOW_RATE_M3_S
    total_co2_kg = 0.0

    data = readings.data
    for i in range(1, len(data)):
        t1 = datetime.fromisoformat(data[i - 1]["timestamp"])
        t2 = datetime.fromisoformat(data[i]["timestamp"])
        duration_seconds = (t2 - t1).total_seconds()

        avg_ppm = (data[i - 1]["co2_ppm"] + data[i]["co2_ppm"]) / 2
        excess_ppm = max(avg_ppm - settings.AMBIENT_CO2_PPM, 0)
        excess_fraction = excess_ppm / 1_000_000

        co2_kg = excess_fraction * flow_rate * duration_seconds * CO2_DENSITY_KG_M3
        total_co2_kg += co2_kg

    actual_emissions_tonnes = total_co2_kg / 1000
    emission_reduction_tonnes = window_baseline_tonnes - actual_emissions_tonnes

    if emission_reduction_tonnes <= 0:
        logger.info(
            f"No reduction for {facility_id}: "
            f"actual={actual_emissions_tonnes:.4f}t, "
            f"baseline={window_baseline_tonnes:.4f}t"
        )
        return None

    logger.info(f"Credit calculation for {facility_id}: {emission_reduction_tonnes:.4f} tonnes")

    return {
        "facility_id": facility_id,
        "credits_issued": round(emission_reduction_tonnes, 4),
        "emission_reduction": round(emission_reduction_tonnes, 4),
        "actual_emissions": round(actual_emissions_tonnes, 4),
        "baseline_used": round(window_baseline_tonnes, 4),
        "period_start": period_start.isoformat(),
        "period_end": period_end.isoformat(),
        "reading_count": len(data)
    }
