package com.carboncredit.app.ui.auditor.notifications

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.ui.manager.notifications.NotificationsScreen
import com.carboncredit.app.ui.manager.notifications.NotificationsViewModel

/**
 * Auditor notifications reuse the same screen as Manager notifications.
 * The data is filtered server-side by Supabase RLS based on the user ID.
 */
@Composable
fun AuditorNotificationsScreen() {
    NotificationsScreen()
}
