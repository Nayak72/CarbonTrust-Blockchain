package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnomalyEvent(
    val id: String = "",
    @SerialName("sensor_id") val sensorId: String = "",
    @SerialName("facility_id") val facilityId: String = "",
    @SerialName("reading_id") val readingId: String = "",
    @SerialName("anomaly_type") val anomalyType: String = "",
    @SerialName("co2_value") val co2Value: Float = 0f,
    @SerialName("z_score") val zScore: Float? = null,
    @SerialName("is_acknowledged") val isAcknowledged: Boolean = false,
    @SerialName("acknowledged_by") val acknowledgedBy: String? = null,
    @SerialName("acknowledged_at") val acknowledgedAt: String? = null,
    @SerialName("acknowledgement_note") val acknowledgementNote: String? = null,
    val timestamp: String = "",
    @SerialName("created_at") val createdAt: String = ""
)
