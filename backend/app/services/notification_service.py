from app.core.supabase_client import get_supabase
from app.core.fcm_client import fcm_client
from app.utils.logger import get_logger

logger = get_logger(__name__)


async def notify_anomaly(
    facility_id: str,
    anomaly_id: str,
    anomaly_type: str,
    sensor_label: str
):
    """
    Send anomaly alert to facility manager and all assigned auditors.
    Creates a notification record in Supabase AND sends an FCM push.
    """
    supabase = get_supabase()

    # Get manager for this facility
    manager_result = supabase.table("user_profiles") \
        .select("id, fcm_token") \
        .eq("facility_id", facility_id) \
        .eq("role", "MANAGER") \
        .execute()

    # Get auditors assigned to this facility
    auditor_ids_result = supabase.table("auditor_assignments") \
        .select("auditor_id") \
        .eq("facility_id", facility_id) \
        .execute()

    auditor_tokens = []
    if auditor_ids_result.data:
        ids = [a["auditor_id"] for a in auditor_ids_result.data]
        auditors = supabase.table("user_profiles") \
            .select("id, fcm_token") \
            .in_("id", ids) \
            .execute()
        auditor_tokens = [a for a in auditors.data if a.get("fcm_token")]

    title = f"⚠️ Anomaly Detected — {sensor_label}"
    body = f"Anomaly type: {anomaly_type.replace('_', ' ').title()}. Review required."

    # Collect all recipients
    recipients = []
    if manager_result.data:
        for manager in manager_result.data:
            if manager.get("fcm_token"):
                recipients.append(manager)
    recipients.extend(auditor_tokens)

    for recipient in recipients:
        # Save to notifications table
        supabase.table("notifications").insert({
            "user_id": recipient["id"],
            "title": title,
            "body": body,
            "type": "anomaly",
            "related_id": anomaly_id
        }).execute()

        # Send FCM push
        if recipient.get("fcm_token"):
            await fcm_client.send_to_token(
                token=recipient["fcm_token"],
                title=title,
                body=body,
                data={"related_id": anomaly_id},
                notification_type="anomaly"
            )

    logger.info(f"Anomaly notifications sent to {len(recipients)} recipients")


async def notify_credit_issued(
    facility_id: str,
    credit_id: str,
    credits_amount: float
):
    """
    Send credit issuance notification to manager and auditors.
    Creates a notification record in Supabase AND sends an FCM push.
    """
    supabase = get_supabase()

    # Get manager for this facility
    manager_result = supabase.table("user_profiles") \
        .select("id, fcm_token") \
        .eq("facility_id", facility_id) \
        .eq("role", "MANAGER") \
        .execute()

    title = "✅ Carbon Credits Issued"
    body = (
        f"{credits_amount:.4f} tonnes of CO₂ credits have been "
        f"verified and recorded on blockchain."
    )

    if manager_result.data:
        for manager in manager_result.data:
            supabase.table("notifications").insert({
                "user_id": manager["id"],
                "title": title,
                "body": body,
                "type": "credit_issued",
                "related_id": credit_id
            }).execute()

            if manager.get("fcm_token"):
                await fcm_client.send_to_token(
                    token=manager["fcm_token"],
                    title=title,
                    body=body,
                    data={"related_id": credit_id},
                    notification_type="credit_issued"
                )

    logger.info(f"Credit issuance notification sent for {credits_amount:.4f} tonnes")
