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


# Singleton instance managed by main.py lifespan
simulator: SensorSimulator | None = None
