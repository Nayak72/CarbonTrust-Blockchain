from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.mqtt_client import mqtt_handler
from app.api.router import api_router
from app.utils.logger import get_logger

logger = get_logger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Manages application startup and shutdown events.
    - Startup: Connects MQTT subscriber to the Mosquitto broker
    - Shutdown: Gracefully disconnects MQTT client
    """
    # Startup
    logger.info("Starting Carbon Credit Backend...")
    mqtt_handler.start()
    logger.info("MQTT subscriber started")
    yield
    # Shutdown
    mqtt_handler.stop()
    logger.info("MQTT subscriber stopped")


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
