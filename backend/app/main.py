from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.mqtt_client import mqtt_handler
from app.config import settings
from app.api.router import api_router
from app.utils.logger import get_logger

logger = get_logger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Manages application startup and shutdown events.
    - Startup: Connects MQTT subscriber and/or starts demo simulators
    - Shutdown: Gracefully disconnects MQTT client and stops all simulators
    """
    # Startup
    logger.info("Starting Carbon Credit Backend...")
    mqtt_handler.start()
    logger.info("MQTT subscriber started")

    from app.core.simulator import simulator_manager
    from app.core.supabase_client import get_supabase

    if settings.SIMULATOR_ENABLED:
        logger.info("Simulator ENABLED — auto-starting for all registered virtual sensors...")
        try:
            supabase = get_supabase()
            # Fetch all active sensors and filter for virtual (SIM_) ones in Python.
            sensors_result = supabase.table("sensors") \
                .select("id, device_id, facility_id, auth_key, sim_auth_key_raw") \
                .eq("is_active", True) \
                .execute()

            virtual_sensors = [
                s for s in (sensors_result.data or [])
                if s.get("device_id", "").startswith("SIM_")
            ]

            started_count = 0
            for sensor in virtual_sensors:
                try:
                    simulator_manager.start(
                        facility_id=sensor["facility_id"],
                        device_id=sensor["device_id"],
                        auth_key=sensor.get("sim_auth_key_raw") or sensor["auth_key"],
                        interval_seconds=settings.SIMULATOR_INTERVAL_SECONDS
                    )
                    started_count += 1
                except Exception as e:
                    logger.error(f"Failed to auto-start simulator for {sensor['device_id']}: {e}")

            logger.info(f"Auto-started {started_count} simulators for virtual sensors")
        except Exception as e:
            logger.error(f"Simulator auto-start failed: {e}")
    else:
        logger.info("Simulator DISABLED (set SIMULATOR_ENABLED=true to enable)")

    yield

    # Shutdown
    mqtt_handler.stop()
    logger.info("MQTT subscriber stopped")

    simulator_manager.stop_all()
    logger.info("All simulators stopped")


app = FastAPI(
    title="Carbon Credit Verification Backend",
    version="1.0.0",
    description=(
        "IoT + Blockchain carbon credit issuance pipeline. "
        "Receives CO₂ sensor data via MQTT, runs anomaly detection, "
        "calculates credits, and records them on Polygon blockchain."
    ),
    lifespan=lifespan
)

# CORS — allow all origins for mobile app access
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"]
)

# Mount all API routes under /api/v1
app.include_router(api_router, prefix="/api/v1")


@app.get("/health")
async def health():
    """Health check endpoint for monitoring and load balancers."""
    return {"status": "ok", "service": "carbon-credit-backend"}

