package com.carboncredit.app.ui.auditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.carboncredit.app.ui.auditor.comparison.FacilityComparisonScreen
import com.carboncredit.app.ui.auditor.dashboard.AuditorDashboardScreen
import com.carboncredit.app.ui.auditor.facility.FacilityDetailScreen
import com.carboncredit.app.ui.auditor.facility.FacilityListScreen
import com.carboncredit.app.ui.auditor.notifications.AuditorNotificationsScreen
import com.carboncredit.app.ui.auditor.profile.AuditorProfileScreen
import com.carboncredit.app.ui.auditor.reports.AuditReportScreen
import com.carboncredit.app.ui.auditor.verification.BlockchainVerificationScreen
import com.carboncredit.app.ui.theme.*

@Composable
fun AuditorNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = AuditorBottomNav.Dashboard.route, modifier = modifier) {
        composable(AuditorBottomNav.Dashboard.route) {
            AuditorDashboardScreen(onFacilityClick = { id -> navController.navigate("facility_detail/$id") })
        }
        composable(AuditorBottomNav.Facilities.route) {
            FacilityListScreen(onFacilityClick = { id -> navController.navigate("facility_detail/$id") })
        }
        composable(AuditorBottomNav.Verify.route) { BlockchainVerificationScreen() }
        composable(AuditorBottomNav.Compare.route) { FacilityComparisonScreen() }
        composable(AuditorBottomNav.More.route) {
            AuditorMoreMenu(
                onNotifications = { navController.navigate("auditor_notifications") },
                onReports = { navController.navigate("audit_reports") },
                onProfile = { navController.navigate("auditor_profile") }
            )
        }
        composable("facility_detail/{facilityId}", arguments = listOf(navArgument("facilityId") { type = NavType.StringType })) { entry ->
            FacilityDetailScreen(facilityId = entry.arguments?.getString("facilityId") ?: "", onBack = { navController.popBackStack() })
        }
        composable("auditor_notifications") { AuditorNotificationsScreen() }
        composable("audit_reports") { AuditReportScreen() }
        composable("auditor_profile") { AuditorProfileScreen() }
    }
}

@Composable
fun AuditorMoreMenu(onNotifications: () -> Unit, onReports: () -> Unit, onProfile: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(16.dp)) {
        Text("More", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        AuditorMenuItem(Icons.Default.Notifications, "Notifications", onNotifications)
        AuditorMenuItem(Icons.Default.Description, "Audit Reports", onReports)
        AuditorMenuItem(Icons.Default.Person, "Profile & Settings", onProfile)
    }
}

@Composable
private fun AuditorMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = BlockchainGold, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
    }
}
