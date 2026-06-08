import firebase_admin
from firebase_admin import credentials, messaging
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)

_initialized = False


def init_firebase():
    """Initialize Firebase Admin SDK (singleton, only runs once)."""
    global _initialized
    if not _initialized:
        try:
            cred = credentials.Certificate(settings.FIREBASE_CREDENTIALS_PATH)
            firebase_admin.initialize_app(cred)
            _initialized = True
            logger.info("Firebase Admin SDK initialized")
        except Exception as e:
            logger.warning(f"Firebase init failed (FCM pushes will be disabled): {e}")


class FCMClient:
    """
    Firebase Cloud Messaging client for sending push notifications
    to Android devices.
    """

    def __init__(self):
        init_firebase()

    async def send_to_token(
        self,
        token: str,
        title: str,
        body: str,
        data: dict = None,
        notification_type: str = "system"
    ) -> bool:
        """
        Sends a push notification to a specific device FCM token.
        Returns True on success, False on failure.
        """
        try:
            message = messaging.Message(
                notification=messaging.Notification(title=title, body=body),
                data={
                    "type": notification_type,
                    **(data or {})
                },
                token=token,
                android=messaging.AndroidConfig(
                    priority="high",
                    notification=messaging.AndroidNotification(
                        channel_id="carbon_credit_alerts",
                        priority="high"
                    )
                )
            )
            response = messaging.send(message)
            logger.info(f"FCM sent successfully: {response}")
            return True
        except Exception as e:
            logger.error(f"FCM send failed: {e}")
            return False


fcm_client = FCMClient()
