package com.carboncredit.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.carboncredit.app.core.utils.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarbonCreditApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            val anomalyChannel = NotificationChannel(
                Constants.CHANNEL_ANOMALY,
                "Anomaly Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for sensor anomalies and suspicious readings"
                enableVibration(true)
            }

            val creditChannel = NotificationChannel(
                Constants.CHANNEL_CREDIT,
                "Credit Issuance",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications when new carbon credits are issued"
            }

            val systemChannel = NotificationChannel(
                Constants.CHANNEL_SYSTEM,
                "System Alerts",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "General system notifications"
            }

            manager.createNotificationChannels(listOf(anomalyChannel, creditChannel, systemChannel))
        }
    }
}
