from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from app.core.simulator import simulator_manager
from app.core.security import get_current_user
from app.utils.logger import get_logger

router = APIRouter(prefix="/simulator", tags=["simulator"])
logger = get_logger(__name__)


class SimulatorStartRequest(BaseModel):
    facility_id: str
    device_id: str
    auth_key: str
    interval_seconds: float = 10.0


class SimulatorStopRequest(BaseModel):
    facility_id: str


@router.post("/start")
async def start_simulator(
    body: SimulatorStartRequest,
    user=Depends(get_current_user)
):
    """
    Start a sensor simulator for a specific facility.
    Called by the Android app after a manager signs up and their facility
    + virtual sensor have been registered.
    Only the manager of that facility can start their simulator.
    """
    # Role check: only managers can start a simulator
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can start simulators")

    # Facility ownership check: manager can only start simulator for their own facility
    if user.get("facility_id") != body.facility_id:
        raise HTTPException(status_code=403, detail="You can only start a simulator for your own facility")

    success = simulator_manager.start(
        facility_id=body.facility_id,
        device_id=body.device_id,
        auth_key=body.auth_key,
        interval_seconds=body.interval_seconds
    )

    if not success:
        raise HTTPException(status_code=500, detail="Failed to start simulator")

    return {
        "status": "started",
        "message": f"Simulator running for facility {body.facility_id} — readings every {body.interval_seconds}s"
    }


@router.post("/stop")
async def stop_simulator(
    body: SimulatorStopRequest,
    user=Depends(get_current_user)
):
    """Stop the simulator for a specific facility."""
    if user.get("role") != "MANAGER":
        raise HTTPException(status_code=403, detail="Only managers can stop simulators")

    if user.get("facility_id") != body.facility_id:
        raise HTTPException(status_code=403, detail="You can only stop your own facility's simulator")

    stopped = simulator_manager.stop(body.facility_id)
    if not stopped:
        return {"status": "not_running", "message": "No simulator was running for this facility"}

    return {"status": "stopped", "message": f"Simulator stopped for facility {body.facility_id}"}


@router.get("/status")
async def get_simulator_status(user=Depends(get_current_user)):
    """
    Returns which facilities currently have running simulators.
    Used for debugging and the profile screen.
    """
    running = simulator_manager.get_running()
    return {
        "running_count": len(running),
        "running_facilities": running
    }
