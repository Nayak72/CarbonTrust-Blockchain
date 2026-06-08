package com.carboncredit.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.carboncredit.app.R
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.data.repository.NotificationRepository
import com.carboncredit.app.ui.manager.ManagerActivity
import com.carboncredit.app.ui.auditor.AuditorActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject lateinit var notificationRepository: NotificationRepository
    @Inject lateinit var tokenManager: TokenManager

    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"] ?: Constants.NOTIF_SYSTEM
        val relatedId = message.data["related_id"]
        val title = message.notification?.title ?: "Carbon Credit Alert"
        val body = message.notification?.body ?: ""

        showNotification(title, body, type, relatedId)

        // Persist to Supabase
        val userId = tokenManager.getUserId()
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    notificationRepository.getNotifications(userId) // trigger refresh
                } catch (_: Exception) { }
            }
        }
    }

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.updateFCMToken(token)
        }
    }

    private fun showNotification(title: String, body: String, type: String, relatedId: String?) {
        val channelId = when (type) {
            Constants.NOTIF_ANOMALY -> Constants.CHANNEL_ANOMALY
            Constants.NOTIF_CREDIT -> Constants.CHANNEL_CREDIT
            else -> Constants.CHANNEL_SYSTEM
        }

        val intent = when (tokenManager.getRole()) {
            Constants.ROLE_MANAGER -> Intent(this, ManagerActivity::class.java)
            Constants.ROLE_AUDITOR -> Intent(this, AuditorActivity::class.java)
            else -> Intent(this, ManagerActivity::class.java)
        }.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("type", type)
            putExtra("related_id", relatedId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
