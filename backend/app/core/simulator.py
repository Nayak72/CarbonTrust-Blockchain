import asyncio
import random
from datetime import datetime, timezone
from app.services.pipeline import run_pipeline
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)


class SensorSimulator:
    """
    Demo sensor simulator that generates realistic CO2/temperature/humidity
    readings and feeds them through the full processing pipeline every N seconds.

    Enable by setting SIMULATOR_ENABLED=true in your .env file and configure
    SIMULATOR_DEVICE_ID and SIMULATOR_AUTH_KEY to match a registered sensor.
    """

    def __init__(
        self,
        device_id: str,
        auth_key: str,
        interval_seconds: float = 10.0
    ):
        self.device_id = device_id
        self.auth_key = auth_key
        self.interval = interval_seconds
        self._task: asyncio.Task | None = None
        self._stop_event = asyncio.Event()

        # Base values for realistic random walk
        self._co2 = 550.0
        self._temperature = 30.0
        self._humidity = 55.0

    def _generate_reading(self) -> dict:
        """Generate a single realistic sensor reading with small variations."""
        # Random walk: nudge the base values slightly
        self._co2 = max(350.0, min(1200.0, self._co2 + random.uniform(-20.0, 20.0)))
        self._temperature = max(20.0, min(45.0, self._temperature + random.uniform(-0.5, 0.5)))
        self._humidity = max(30.0, min(80.0, self._humidity + random.uniform(-1.0, 1.0)))

        return {
            "device_id": self.device_id,
            "auth_key": self.auth_key,
            "co2_ppm": round(self._co2, 1),
            "temperature": round(self._temperature, 1),
            "humidity": round(self._humidity, 1),
            "timestamp": datetime.now(timezone.utc).isoformat().replace("+00:00", "Z")
        }

    async def _loop(self):
        """Main simulator loop."""
        logger.info(
            f"Simulator started — device={self.device_id}, "
            f"interval={self.interval}s"
        )

        while not self._stop_event.is_set():
            try:
                payload = self._generate_reading()
                logger.info(
                    f"Simulator sending reading: CO2={payload['co2_ppm']} ppm, "
                    f"T={payload['temperature']}C, H={payload['humidity']}%"
                )
                await run_pipeline(payload)
            except Exception as e:
                logger.error(f"Simulator pipeline error: {e}")

            try:
                await asyncio.wait_for(
                    self._stop_event.wait(),
                    timeout=self.interval
                )
            except asyncio.TimeoutError:
                continue

        logger.info("Simulator stopped")

    def start(self):
        """Start the simulator background task."""
        if self._task is not None and not self._task.done():
            logger.warning("Simulator already running")
            return

        self._stop_event.clear()
        self._task = asyncio.create_task(self._loop())

    def stop(self):
        """Signal the simulator to stop gracefully."""
        if self._task is None:
            return
        self._stop_event.set()
        self._task = None



# Singleton instance managed by main.py lifespan (legacy — kept for backward compat)
simulator: SensorSimulator | None = None


class SimulatorManager:
    """
    Manages multiple SensorSimulator instances — one per registered facility.
    Called by the /simulator/start and /simulator/stop API endpoints,
    and on backend startup to resume all active facilities.
    """

    def __init__(self):
        # Key: facility_id, Value: SensorSimulator instance
        self._simulators: dict[str, SensorSimulator] = {}

    def start(
        self,
        facility_id: str,
        device_id: str,
        auth_key: str,
        interval_seconds: float = 10.0
    ) -> bool:
        """
        Start a simulator for a facility.
        If one is already running for this facility, stops it first and restarts.
        Returns True if started successfully.
        """
        # Stop existing if running
        if facility_id in self._simulators:
            self._simulators[facility_id].stop()

        sim = SensorSimulator(
            device_id=device_id,
            auth_key=auth_key,
            interval_seconds=interval_seconds
        )
        sim.start()
        self._simulators[facility_id] = sim
        logger.info(f"Simulator started for facility: {facility_id}, device: {device_id}")
        return True

    def stop(self, facility_id: str) -> bool:
        """
        Stop the simulator for a specific facility.
        Returns True if it was running and stopped, False if not found.
        """
        if facility_id not in self._simulators:
            return False
        self._simulators[facility_id].stop()
        del self._simulators[facility_id]
        logger.info(f"Simulator stopped for facility: {facility_id}")
        return True

    def stop_all(self):
        """Stop all running simulators. Called on backend shutdown."""
        for facility_id, sim in list(self._simulators.items()):
            sim.stop()
            logger.info(f"Simulator stopped for facility: {facility_id}")
        self._simulators.clear()

    def get_running(self) -> list[str]:
        """Return list of facility_ids that currently have running simulators."""
        return list(self._simulators.keys())

    def is_running(self, facility_id: str) -> bool:
        return facility_id in self._simulators


# Singleton instance
simulator_manager = SimulatorManager()
