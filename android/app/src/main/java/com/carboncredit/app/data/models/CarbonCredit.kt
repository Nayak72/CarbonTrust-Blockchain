package com.carboncredit.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarbonCredit(
    val id: String = "",
    @SerialName("facility_id") val facilityId: String = "",
    @SerialName("credits_issued") val creditsIssued: Float = 0f,
    @SerialName("credits_raw") val creditsRaw: Float? = null,
    @SerialName("quality_factor") val qualityFactor: Float? = null,
    @SerialName("emission_reduction") val emissionReduction: Float = 0f,
    @SerialName("actual_emissions") val actualEmissions: Float = 0f,
    @SerialName("baseline_used") val baselineUsed: Float = 0f,
    @SerialName("report_hash") val reportHash: String? = null,
    @SerialName("period_id") val periodId: String? = null,
    @SerialName("period_start") val periodStart: String = "",
    @SerialName("period_end") val periodEnd: String = "",
    @SerialName("ipfs_cid") val ipfsCid: String = "",
    @SerialName("tx_hash") val txHash: String = "",
    @SerialName("block_number") val blockNumber: Long? = null,
    val status: String = "verified",
    @SerialName("created_at") val createdAt: String = ""
)
