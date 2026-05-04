package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Facility(
    val id: String = "",
    val name: String = "",
    @SerialName("company_name") val companyName: String = "",
    val location: String? = null,
    @SerialName("industry_type") val industryType: String? = null,
    @SerialName("baseline_emissions") val baselineEmissions: Float = 0f,
    @SerialName("created_at") val createdAt: String = ""
)
