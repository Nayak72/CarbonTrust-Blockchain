package com.carboncredit.app.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    @SerialName("full_name") @SerializedName("full_name") val fullName: String = "",
    val email: String = "",
    val role: String = "",
    @SerialName("facility_id") @SerializedName("facility_id") val facilityId: String? = null,
    @SerialName("avatar_url") @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerialName("created_at") @SerializedName("created_at") val createdAt: String = ""
)
