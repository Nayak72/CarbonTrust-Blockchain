import asyncio
import json
import ssl
import paho.mqtt.client as mqtt
from app.config import settings
from app.utils.logger import get_logger
from app.services.pipeline import run_pipeline

logger = get_logger(__name__)


class MQTTHandler:
    """
    MQTT subscriber that connects to the Mosquitto broker and
    dispatches incoming sensor readings to the processing pipeline.
    """

    def __init__(self):
        self.client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
        self.client.username_pw_set(settings.MQTT_USERNAME, settings.MQTT_PASSWORD)

        if settings.MQTT_TLS_ENABLED:
            context = ssl.create_default_context(cafile=settings.MQTT_CA_CERT_PATH)
            self.client.tls_set_context(context)

        self.client.on_connect = self._on_connect
        self.client.on_message = self._on_message
        self.loop = None

    def _on_connect(self, client, userdata, flags, reason_code, properties):
        if reason_code == 0:
            logger.info("MQTT connected successfully")
            client.subscribe("factory/+/readings", qos=1)
            logger.info("Subscribed to factory/+/readings")
        else:
            logger.error(f"MQTT connection failed: {reason_code}")

    def _on_message(self, client, userdata, msg):
        try:
            payload = json.loads(msg.payload.decode())
            logger.info(f"MQTT message received: {msg.topic}")
            # Schedule async pipeline on the event loop
            if self.loop:
                asyncio.run_coroutine_threadsafe(run_pipeline(payload), self.loop)
        except json.JSONDecodeError as e:
            logger.error(f"Invalid MQTT payload: {e}")
        except Exception as e:
            logger.error(f"MQTT message handling error: {e}")

    def start(self):
        """Start the MQTT client and connect to the broker."""
        try:
            self.loop = asyncio.get_event_loop()
        except RuntimeError:
            self.loop = asyncio.new_event_loop()

        try:
            self.client.connect(
                settings.MQTT_BROKER_HOST,
                settings.MQTT_BROKER_PORT,
                keepalive=60
            )
            self.client.loop_start()
            logger.info("MQTT client started")
        except Exception as e:
            logger.warning(f"MQTT connection failed (broker may not be running): {e}")
            logger.info("Backend will continue without MQTT — use REST API for manual submissions")

    def stop(self):
        """Stop the MQTT client gracefully."""
        self.client.loop_stop()
        self.client.disconnect()
        logger.info("MQTT client stopped")


mqtt_handler = MQTTHandler()
