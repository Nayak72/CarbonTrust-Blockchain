package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "",
    @SerialName("related_id") val relatedId: String? = null,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("created_at") val createdAt: String = ""
)
