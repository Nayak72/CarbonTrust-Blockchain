from fastapi import APIRouter
from app.api.routes import auth, ipfs, credits, sensors, readings, facilities, anomalies

api_router = APIRouter()

# Include all route modules
api_router.include_router(auth.router)
api_router.include_router(ipfs.router)
api_router.include_router(credits.router)
api_router.include_router(sensors.router)
api_router.include_router(readings.router)
api_router.include_router(facilities.router)
api_router.include_router(anomalies.router)
