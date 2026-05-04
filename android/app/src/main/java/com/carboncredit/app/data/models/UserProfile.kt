package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    @SerialName("full_name") val fullName: String = "",
    val email: String = "",
    val role: String = "",
    @SerialName("facility_id") val facilityId: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("created_at") val createdAt: String = ""
)
