package com.carboncredit.app.data.repository

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.network.FCMTokenRequest
import com.carboncredit.app.data.models.Notification
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val apiService: ApiService
) {

    suspend fun getNotifications(userId: String, type: String? = null): List<Notification> {
        return supabase.postgrest["notifications"]
            .select {
                filter {
                    eq("user_id", userId)
                    if (type != null) eq("type", type)
                }
                order("created_at", Order.DESCENDING)
                limit(100)
            }
            .decodeList<Notification>()
    }

    suspend fun getUnreadCount(userId: String): Int {
        return supabase.postgrest["notifications"]
            .select { filter { eq("user_id", userId); eq("is_read", false) } }
            .decodeList<Notification>().size
    }

    suspend fun markAsRead(notificationId: String) {
        supabase.postgrest["notifications"]
            .update({ set("is_read", true) }) { filter { eq("id", notificationId) } }
    }

    suspend fun markAllAsRead(userId: String) {
        supabase.postgrest["notifications"]
            .update({ set("is_read", true) }) { filter { eq("user_id", userId); eq("is_read", false) } }
    }

    suspend fun updateFCMToken(token: String) {
        try {
            apiService.registerFCMToken(FCMTokenRequest(fcm_token = token))
        } catch (_: Exception) { }
    }
}
