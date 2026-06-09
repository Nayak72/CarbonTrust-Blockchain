package com.carboncredit.app.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carboncredit.app.ui.admin.dashboard.AdminDashboardScreen
import com.carboncredit.app.ui.admin.auditors.AdminAuditorsScreen

@Composable
fun AdminNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AdminBottomNav.Dashboard.route,
        modifier = modifier
    ) {
        composable(AdminBottomNav.Dashboard.route) {
            AdminDashboardScreen()
        }
        
        composable(AdminBottomNav.Auditors.route) {
            AdminAuditorsScreen()
        }
        
        composable(AdminBottomNav.Profile.route) {
            com.carboncredit.app.ui.manager.profile.ProfileScreen()
        }
    }
}
