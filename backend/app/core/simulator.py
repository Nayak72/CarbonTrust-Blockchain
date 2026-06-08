import asyncio
import math
import random
from datetime import datetime, timezone
from app.services.pipeline import run_pipeline
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)


class SensorSimulator:
    """
    Physics-informed sensor simulator that generates realistic CO₂, temperature,
    and humidity readings and feeds them through the full processing pipeline.

    Uses an Ornstein-Uhlenbeck mean-reversion process for CO₂ drift, a diurnal
    emission curve based on facility shift hours, and optional anomaly injection
    for demo/testing purposes.
    """

    def __init__(
        self,
        device_id: str,
        auth_key: str,
        interval_seconds: float = 10.0,
        facility_type: str = "manufacturing",
        baseline_ppm: float = 600.0,
        inject_anomaly_rate: float = 0.02,
        diurnal_enabled: bool = True,
    ):
        self.device_id = device_id
        self.auth_key = auth_key
        self.interval = interval_seconds
        self.facility_type = facility_type
        self.baseline_ppm = baseline_ppm
        self.inject_anomaly_rate = inject_anomaly_rate
        self.diurnal_enabled = diurnal_enabled

        self._task: asyncio.Task | None = None
        self._stop_event = asyncio.Event()

        # Initial state — start at baseline with realistic offsets
        self._co2 = baseline_ppm
        self._temperature = 28.0
        self._humidity = 60.0

    def _generate_reading(self) -> dict:
        """
        Generate a single realistic sensor reading.

        Physics model:
          1. Diurnal curve — factory emissions peak at shift hours (08:00–17:00)
          2. Ornstein-Uhlenbeck mean reversion — CO₂ drifts toward diurnal target
          3. Anomaly injection — optional spike / frozen / zero for demo
          4. Temperature — loosely follows CO₂ (combustion correlation)
          5. Humidity — inverse correlation with temperature
        """
        now = datetime.now(timezone.utc)
        hour = now.hour

        # 1. Diurnal curve
        if self.diurnal_enabled:
            if 8 <= hour < 17:
                # Peak production hours: sinusoidal curve peaking at midday
                diurnal_factor = 1.0 + 0.3 * math.sin(math.pi * (hour - 8) / 9)
            elif 17 <= hour < 22:
                diurnal_factor = 0.7   # Wind-down after shift
            else:
                diurnal_factor = 0.4   # Night-time low
        else:
            diurnal_factor = 1.0

        # 2. Slow-drift baseline using Ornstein-Uhlenbeck mean reversion
        target = self.baseline_ppm * diurnal_factor
        self._co2 += 0.1 * (target - self._co2) + random.gauss(0, 8.0)
        self._co2 = max(350.0, min(1400.0, self._co2))

        # 3. Anomaly injection (for demo / testing)
        anomaly_injected = False
        if random.random() < self.inject_anomaly_rate:
            anomaly_type = random.choice(["spike", "frozen", "zero"])
            if anomaly_type == "spike":
                self._co2 = self._co2 * random.uniform(2.5, 4.0)
            elif anomaly_type == "frozen":
                pass  # self._co2 unchanged — will repeat (frozen value detection)
            else:
                self._co2 = 0.0
            anomaly_injected = True

        # 4. Temperature: loosely follows CO₂ (combustion processes raise temperature)
        self._temperature += (
            0.05 * (28.0 + diurnal_factor * 8 - self._temperature)
            + random.gauss(0, 0.3)
        )
        self._temperature = max(18.0, min(50.0, self._temperature))

        # 5. Humidity: inverse correlation with temperature (warmer → drier)
        self._humidity += (
            0.05 * (65.0 - diurnal_factor * 15 - self._humidity)
            + random.gauss(0, 0.5)
        )
        self._humidity = max(20.0, min(90.0, self._humidity))

        return {
            "device_id": self.device_id,
            "auth_key": self.auth_key,
            "co2_ppm": round(self._co2, 1),
            "temperature": round(self._temperature, 1),
            "humidity": round(self._humidity, 1),
            "timestamp": now.isoformat().replace("+00:00", "Z"),
            "_sim_anomaly_injected": anomaly_injected,  # stripped before pipeline
        }

    async def _loop(self):
        """Main simulator loop."""
        logger.info(
            f"Simulator started — device={self.device_id}, "
            f"facility_type={self.facility_type}, baseline_ppm={self.baseline_ppm}, "
            f"interval={self.interval}s, anomaly_rate={self.inject_anomaly_rate}"
        )

        while not self._stop_event.is_set():
            try:
                reading = self._generate_reading()

                # Strip the internal sim metadata before passing to the pipeline
                anomaly_injected = reading.pop("_sim_anomaly_injected", False)
                if anomaly_injected:
                    logger.debug(f"Simulator injected anomaly: CO2={reading['co2_ppm']}")

                logger.info(
                    f"Simulator reading: CO2={reading['co2_ppm']} ppm, "
                    f"T={reading['temperature']}°C, H={reading['humidity']}%"
                )
                await run_pipeline(reading)
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
        interval_seconds: float = 10.0,
        facility_type: str = "manufacturing",
        baseline_ppm: float = 600.0,
        inject_anomaly_rate: float = 0.02,
    ) -> bool:
        """
        Start a physics-informed simulator for a facility.
        If one is already running for this facility, stops it first and restarts.
        Returns True if started successfully.
        """
        # Stop existing if running
        if facility_id in self._simulators:
            self._simulators[facility_id].stop()

        sim = SensorSimulator(
            device_id=device_id,
            auth_key=auth_key,
            interval_seconds=interval_seconds,
            facility_type=facility_type,
            baseline_ppm=baseline_ppm,
            inject_anomaly_rate=inject_anomaly_rate,
        )
        sim.start()
        self._simulators[facility_id] = sim
        logger.info(
            f"Simulator started for facility: {facility_id}, device: {device_id}, "
            f"type: {facility_type}, baseline: {baseline_ppm} ppm"
        )
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
