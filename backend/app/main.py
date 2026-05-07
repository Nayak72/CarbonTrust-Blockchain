from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.mqtt_client import mqtt_handler
from app.core.simulator import SensorSimulator, simulator
from app.config import settings
from app.api.router import api_router
from app.utils.logger import get_logger

logger = get_logger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Manages application startup and shutdown events.
    - Startup: Connects MQTT subscriber and/or starts demo simulator
    - Shutdown: Gracefully disconnects MQTT client and stops simulator
    """
    # Startup
    logger.info("Starting Carbon Credit Backend...")
    mqtt_handler.start()
    logger.info("MQTT subscriber started")

    if settings.SIMULATOR_ENABLED:
        global simulator
        simulator = SensorSimulator(
            device_id=settings.SIMULATOR_DEVICE_ID,
            auth_key=settings.SIMULATOR_AUTH_KEY,
            interval_seconds=settings.SIMULATOR_INTERVAL_SECONDS
        )
        simulator.start()
        logger.info(
            f"Sensor simulator ENABLED — device={settings.SIMULATOR_DEVICE_ID}, "
            f"interval={settings.SIMULATOR_INTERVAL_SECONDS}s. "
            f"Make sure this device is registered via POST /api/v1/sensors/register"
        )
    else:
        logger.info("Sensor simulator DISABLED (set SIMULATOR_ENABLED=true to enable)")

    yield

    # Shutdown
    mqtt_handler.stop()
    logger.info("MQTT subscriber stopped")

    if simulator is not None:
        simulator.stop()
        logger.info("Simulator stopped")


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
