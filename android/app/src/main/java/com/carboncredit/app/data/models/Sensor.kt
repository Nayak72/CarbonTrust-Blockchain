package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sensor(
    val id: String = "",
    @SerialName("facility_id") val facilityId: String = "",
    @SerialName("device_id") val deviceId: String = "",
    @SerialName("auth_key") val authKey: String = "",
    @SerialName("location_label") val locationLabel: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("last_seen") val lastSeen: String? = null,
    @SerialName("created_at") val createdAt: String = ""
)
