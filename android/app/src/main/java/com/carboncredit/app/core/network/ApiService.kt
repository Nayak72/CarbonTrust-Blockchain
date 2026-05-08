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

    /** User Signup */
    @POST("auth/signup")
    suspend fun signup(@Body body: UserSignupRequest): TokenResponse

    /** User Login */
    @POST("auth/login")
    suspend fun login(@Body body: UserLoginRequest): TokenResponse

    /** Get current user's profile */
    @GET("auth/me")
    suspend fun getMe(): com.carboncredit.app.data.models.UserProfile

    /** Get facility by ID */
    @GET("facilities/{facility_id}")
    suspend fun getFacilityById(@Path("facility_id") facilityId: String): com.carboncredit.app.data.models.Facility

    /** List all facilities (Auditor) */
    @GET("facilities/")
    suspend fun listFacilities(): List<com.carboncredit.app.data.models.Facility>

    /** Register or update FCM token for push notifications */
    @POST("auth/fcm-token")
    suspend fun registerFCMToken(@Body body: FCMTokenRequest): FCMTokenResponse

    /** Fetch IPFS report content (FastAPI proxies to avoid CORS/rate limits) */
    @GET("ipfs/report/{cid}")
    suspend fun fetchIPFSReport(@Path("cid") cid: String): IPFSReportResponse

    /** Trigger manual credit recalculation for a facility */
    @POST("credits/recalculate/{facility_id}")
    suspend fun triggerRecalculation(@Path("facility_id") facilityId: String): RecalculationResponse

    /** Create a new facility during manager sign-up (no auth required — called before user_profiles exists) */
    @POST("facilities/onboard")
    suspend fun onboardFacility(@Body body: FacilityOnboardRequest): FacilityCreateResponse

    /** Register a sensor device for a facility */
    @POST("sensors/register")
    suspend fun registerSensor(@Body body: SensorRegisterRequest): SensorRegisterResponse

    /** Start a simulator for a facility's virtual sensor */
    @POST("simulator/start")
    suspend fun startSimulator(@Body body: SimulatorStartRequest): SimulatorStartResponse
}

// --- Request/Response DTOs ---

data class UserSignupRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val role: String,
    val facility_id: String? = null
)

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val user: com.carboncredit.app.data.models.UserProfile
)

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

data class FacilityOnboardRequest(
    val name: String,
    val company_name: String,
    val location: String,
    val industry_type: String,
    val baseline_emissions: Float
)

data class FacilityCreateRequest(
    val name: String,
    val company_name: String,
    val location: String,
    val industry_type: String,
    val baseline_emissions: Float
)

data class FacilityCreateResponse(
    val status: String,
    val facility: FacilityData
)

data class FacilityData(
    val id: String,
    val name: String,
    val company_name: String
)

data class SensorRegisterRequest(
    val device_id: String,
    val auth_key: String,
    val location_label: String,
    val facility_id: String
)

data class SensorRegisterResponse(
    val status: String,
    val sensor: SensorData
)

data class SensorData(
    val id: String,
    val device_id: String
)

data class SimulatorStartRequest(
    val facility_id: String,
    val device_id: String,
    val auth_key: String
)

data class SimulatorStartResponse(
    val status: String,
    val message: String
)

