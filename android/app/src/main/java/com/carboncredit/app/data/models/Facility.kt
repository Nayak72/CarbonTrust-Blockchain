package com.carboncredit.app.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Facility(
    val id: String = "",
    val name: String = "",
    @SerialName("company_name") @SerializedName("company_name") val companyName: String = "",
    val location: String? = null,
    @SerialName("industry_type") @SerializedName("industry_type") val industryType: String? = null,
    @SerialName("baseline_emissions") @SerializedName("baseline_emissions") val baselineEmissions: Float = 0f,
    @SerialName("created_at") @SerializedName("created_at") val createdAt: String = ""
)
