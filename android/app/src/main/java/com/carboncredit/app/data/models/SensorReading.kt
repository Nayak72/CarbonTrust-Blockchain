package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SensorReading(
    val id: String = "",
    @SerialName("sensor_id") val sensorId: String = "",
    @SerialName("facility_id") val facilityId: String = "",
    @SerialName("co2_ppm") val co2Ppm: Float = 0f,
    val temperature: Float = 0f,
    val humidity: Float = 0f,
    @SerialName("is_anomaly") val isAnomaly: Boolean = false,
    @SerialName("anomaly_type") val anomalyType: String? = null,
    @SerialName("z_score") val zScore: Float? = null,
    val timestamp: String = "",
    @SerialName("created_at") val createdAt: String = ""
)
