package com.carboncredit.app.data.repository

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.network.FacilityOnboardRequest
import com.carboncredit.app.core.network.SensorRegisterRequest
import com.carboncredit.app.core.network.SimulatorStartRequest
import com.carboncredit.app.core.network.UserLoginRequest
import com.carboncredit.app.core.network.UserSignupRequest
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.CompanyInfo
import com.carboncredit.app.data.models.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val tokenManager: TokenManager,
    private val apiService: ApiService
) {

    suspend fun signIn(email: String, password: String): UserProfile {
        val response = apiService.login(UserLoginRequest(email, password))
        
        val profile = response.user
        val userId = profile.id

        tokenManager.saveToken(response.access_token)
        tokenManager.saveRole(profile.role)
        tokenManager.saveUserId(userId)
        tokenManager.saveUserName(profile.fullName)
        if (profile.facilityId != null) {
            tokenManager.saveFacilityId(profile.facilityId)
        }

        return profile
    }

    suspend fun signOut() {
        tokenManager.clear()
    }

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String,
        role: String,
        companyInfo: CompanyInfo? = null
    ): UserProfile {
        var facilityId: String? = null

        // Step 1: If MANAGER, create facility first to get facilityId
        if (role == "MANAGER" && companyInfo != null) {
            val facilityResponse = apiService.onboardFacility(
                FacilityOnboardRequest(
                    name = companyInfo.facilityName,
                    company_name = companyInfo.companyName,
                    location = companyInfo.location,
                    industry_type = companyInfo.industryType,
                    baseline_emissions = companyInfo.baselineEmissions
                )
            )
            facilityId = facilityResponse.facility.id
        }

        // Step 2: Create user profile via FastAPI /signup
        // This handles password hashing, database insert, and returns the JWT token
        val signupResponse = apiService.signup(
            UserSignupRequest(
                full_name = fullName,
                email = email,
                password = password,
                role = role,
                facility_id = facilityId
            )
        )
        
        val profile = signupResponse.user
        val userId = profile.id
        
        // Save the token IMMEDIATELY so subsequent API calls use it
        tokenManager.saveToken(signupResponse.access_token)
        tokenManager.saveRole(profile.role)
        tokenManager.saveUserId(userId)
        tokenManager.saveUserName(profile.fullName)
        if (profile.facilityId != null) {
            tokenManager.saveFacilityId(profile.facilityId)
        }

        // Step 3: If MANAGER, generate sensor and start simulator
        if (role == "MANAGER" && companyInfo != null && facilityId != null) {
            // 3a: Generate virtual sensor credentials
            val deviceId = "SIM_${facilityId.take(8).uppercase()}"
            val authKey = java.util.UUID.randomUUID().toString()

            // 3b: Register virtual sensor via FastAPI (now authenticated!)
            apiService.registerSensor(
                SensorRegisterRequest(
                    device_id = deviceId,
                    auth_key = authKey,
                    location_label = "Simulated Chimney Sensor",
                    facility_id = facilityId
                )
            )

            // 3c: Start simulator for this facility via FastAPI
            try {
                apiService.startSimulator(
                    SimulatorStartRequest(
                        facility_id = facilityId,
                        device_id = deviceId,
                        auth_key = authKey
                    )
                )
            } catch (e: Exception) {
                // Simulator start failure is non-fatal
                android.util.Log.w("AuthRepository", "Simulator start failed (non-fatal): ${e.message}")
            }
        }

        return profile
    }

    suspend fun updatePassword(newPassword: String) {
        // Not implemented in custom JWT flow yet
        throw UnsupportedOperationException("Password update not implemented yet")
    }

    suspend fun getCurrentProfile(): UserProfile {
        val userId = tokenManager.getUserId() ?: throw Exception("Not logged in")
        return apiService.getMe()
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
    fun getRole(): String? = tokenManager.getRole()
    fun getFacilityId(): String? = tokenManager.getFacilityId()
    fun getUserId(): String? = tokenManager.getUserId()
    fun getUserName(): String? = tokenManager.getUserName()
}

