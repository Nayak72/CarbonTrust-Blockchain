from fastapi import APIRouter
from app.api.routes import auth, ipfs, credits, sensors, readings, facilities, anomalies
from app.api.routes.simulator import router as simulator_router
from app.api.routes.assignments import router as assignments_router
from app.api.routes.admin import router as admin_router

api_router = APIRouter()

# Include all route modules
api_router.include_router(auth.router)
api_router.include_router(ipfs.router)
api_router.include_router(credits.router)
api_router.include_router(sensors.router)
api_router.include_router(readings.router)
api_router.include_router(facilities.router)
api_router.include_router(anomalies.router)
api_router.include_router(simulator_router)
api_router.include_router(assignments_router)
api_router.include_router(admin_router)

