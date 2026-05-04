package com.carboncredit.app.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * FastAPI endpoint interfaces.
 * Only operations requiring backend business logic go here.
 * Most data reads go through Supabase SDK directly.
 */
interface ApiService {

    /** Register or update FCM token for push notifications */
    @POST("auth/fcm-token")
    suspend fun registerFCMToken(@Body body: FCMTokenRequest): FCMTokenResponse

    /** Fetch IPFS report content (FastAPI proxies to avoid CORS/rate limits) */
    @GET("ipfs/report/{cid}")
    suspend fun fetchIPFSReport(@Path("cid") cid: String): IPFSReportResponse

    /** Trigger manual credit recalculation for a facility */
    @POST("credits/recalculate/{facility_id}")
    suspend fun triggerRecalculation(@Path("facility_id") facilityId: String): RecalculationResponse
}

// --- Request/Response DTOs ---

data class FCMTokenRequest(
    val fcm_token: String
)

data class FCMTokenResponse(
    val success: Boolean,
    val message: String
)

data class IPFSReportResponse(
    val content: String,
    val cid: String,
    val content_type: String
)

data class RecalculationResponse(
    val success: Boolean,
    val credits_issued: Float?,
    val message: String
)
