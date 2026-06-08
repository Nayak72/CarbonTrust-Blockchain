package com.carboncredit.app.ui.auditor.profile

import androidx.compose.runtime.Composable
import com.carboncredit.app.ui.manager.profile.ProfileScreen

/**
 * Auditor profile reuses the same profile screen as Manager.
 * Profile data is fetched based on the authenticated user's ID.
 */
@Composable
fun AuditorProfileScreen() {
    ProfileScreen()
}
