package com.carboncredit.app.data.repository

import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val tokenManager: TokenManager
) {

    suspend fun signIn(email: String, password: String): UserProfile {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val session = supabase.auth.currentSessionOrNull()
            ?: throw Exception("No session after login")

        val userId = session.user?.id ?: throw Exception("No user ID in session")

        val profile = supabase.postgrest["user_profiles"]
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfile>()

        tokenManager.saveToken(session.accessToken)
        tokenManager.saveRole(profile.role)
        tokenManager.saveUserId(userId)
        tokenManager.saveUserName(profile.fullName)
        if (profile.facilityId != null) {
            tokenManager.saveFacilityId(profile.facilityId)
        }

        return profile
    }

    suspend fun signOut() {
        try {
            supabase.auth.signOut()
        } catch (_: Exception) {
            // Sign out locally even if network fails
        }
        tokenManager.clear()
    }

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String,
        role: String
    ): UserProfile {
        // Create Supabase Auth user
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        // Sign in immediately to get session
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val session = supabase.auth.currentSessionOrNull()
            ?: throw Exception("No session after sign up")

        val userId = session.user?.id ?: throw Exception("No user ID in session")

        // Create user_profiles row
        supabase.postgrest["user_profiles"]
            .insert(mapOf(
                "id" to userId,
                "full_name" to fullName,
                "email" to email,
                "role" to role
            ))

        // Fetch back the created profile
        val profile = supabase.postgrest["user_profiles"]
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfile>()

        // Save session data locally
        tokenManager.saveToken(session.accessToken)
        tokenManager.saveRole(profile.role)
        tokenManager.saveUserId(userId)
        tokenManager.saveUserName(profile.fullName)
        if (profile.facilityId != null) {
            tokenManager.saveFacilityId(profile.facilityId)
        }

        return profile
    }

    suspend fun updatePassword(newPassword: String) {
        supabase.auth.updateUser {
            password = newPassword
        }
    }

    suspend fun getCurrentProfile(): UserProfile {
        val userId = tokenManager.getUserId() ?: throw Exception("Not logged in")
        return supabase.postgrest["user_profiles"]
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfile>()
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
    fun getRole(): String? = tokenManager.getRole()
    fun getFacilityId(): String? = tokenManager.getFacilityId()
    fun getUserId(): String? = tokenManager.getUserId()
    fun getUserName(): String? = tokenManager.getUserName()
}
