package com.carboncredit.app.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) = prefs.edit().putString(KEY_JWT, token).apply()
    fun getToken(): String? = prefs.getString(KEY_JWT, null)

    fun saveRole(role: String) = prefs.edit().putString(KEY_ROLE, role).apply()
    fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    fun saveFacilityId(id: String) = prefs.edit().putString(KEY_FACILITY_ID, id).apply()
    fun getFacilityId(): String? = prefs.getString(KEY_FACILITY_ID, null)

    fun saveUserId(id: String) = prefs.edit().putString(KEY_USER_ID, id).apply()
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun saveUserName(name: String) = prefs.edit().putString(KEY_USER_NAME, name).apply()
    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    fun isLoggedIn(): Boolean = getToken() != null

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_JWT = "jwt"
        private const val KEY_ROLE = "role"
        private const val KEY_FACILITY_ID = "facility_id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
    }
}
