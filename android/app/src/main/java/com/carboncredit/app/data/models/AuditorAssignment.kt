package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuditorAssignment(
    val id: String = "",
    @SerialName("auditor_id") val auditorId: String = "",
    @SerialName("facility_id") val facilityId: String = "",
    @SerialName("assigned_at") val assignedAt: String = ""
)
