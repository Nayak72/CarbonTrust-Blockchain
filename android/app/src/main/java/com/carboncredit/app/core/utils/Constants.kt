package com.carboncredit.app.core.utils

object Constants {
    // Roles
    const val ROLE_MANAGER = "MANAGER"
    const val ROLE_AUDITOR = "AUDITOR"

    // Anomaly Types
    const val ANOMALY_ZSCORE = "zscore_breach"
    const val ANOMALY_FROZEN = "frozen_value"
    const val ANOMALY_ZERO = "zero_reading"
    const val ANOMALY_CONNECTIVITY = "connectivity_gap"

    // Credit Status
    const val STATUS_VERIFIED = "verified"
    const val STATUS_PENDING = "pending"
    const val STATUS_DISPUTED = "disputed"

    // Notification Types
    const val NOTIF_ANOMALY = "anomaly"
    const val NOTIF_CREDIT = "credit_issued"
    const val NOTIF_SYSTEM = "system"

    // CO2 Thresholds (ppm)
    const val CO2_NORMAL_MAX = 600f
    const val CO2_WARNING_MAX = 900f

    // Compliance Thresholds
    const val COMPLIANCE_THRESHOLD = 0.85f  // below 85% = compliant
    const val WATCH_THRESHOLD = 1.0f        // 85-100% = watch, above = non-compliant

    // Notification Channels
    const val CHANNEL_ANOMALY = "anomaly_alerts"
    const val CHANNEL_CREDIT = "credit_alerts"
    const val CHANNEL_SYSTEM = "system_alerts"

    // PolygonScan
    const val POLYGONSCAN_BASE_URL = "https://amoy.polygonscan.com/tx/"

    // Time periods
    const val PERIOD_TODAY = "today"
    const val PERIOD_7_DAYS = "7days"
    const val PERIOD_30_DAYS = "30days"
    const val PERIOD_CUSTOM = "custom"
}
