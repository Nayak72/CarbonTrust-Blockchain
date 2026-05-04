package com.carboncredit.app.ui.manager

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.carboncredit.app.ui.manager.analytics.EmissionAnalyticsScreen
import com.carboncredit.app.ui.manager.anomalies.AnomalyLogScreen
import com.carboncredit.app.ui.manager.credits.CreditDetailScreen
import com.carboncredit.app.ui.manager.credits.CreditLedgerScreen
import com.carboncredit.app.ui.manager.dashboard.DashboardScreen
import com.carboncredit.app.ui.manager.notifications.NotificationsScreen
import com.carboncredit.app.ui.manager.profile.ProfileScreen
import com.carboncredit.app.ui.manager.sensors.SensorDetailScreen
import com.carboncredit.app.ui.manager.sensors.SensorListScreen

@Composable
fun ManagerNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ManagerBottomNav.Dashboard.route,
        modifier = modifier
    ) {
        // Bottom nav destinations
        composable(ManagerBottomNav.Dashboard.route) {
            DashboardScreen(
                onSensorClick = { sensorId -> navController.navigate("sensor_detail/$sensorId") },
                onAnomalyClick = { navController.navigate("anomaly_log") }
            )
        }

        composable(ManagerBottomNav.Sensors.route) {
            SensorListScreen(
                onSensorClick = { sensorId -> navController.navigate("sensor_detail/$sensorId") }
            )
        }

        composable(ManagerBottomNav.Analytics.route) {
            EmissionAnalyticsScreen()
        }

        composable(ManagerBottomNav.Credits.route) {
            CreditLedgerScreen(
                onCreditClick = { creditId -> navController.navigate("credit_detail/$creditId") }
            )
        }

        composable(ManagerBottomNav.More.route) {
            MoreMenuScreen(
                onNotifications = { navController.navigate("notifications") },
                onAnomalyLog = { navController.navigate("anomaly_log") },
                onProfile = { navController.navigate("profile") }
            )
        }

        // Detail destinations
        composable(
            route = "sensor_detail/{sensorId}",
            arguments = listOf(navArgument("sensorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sensorId = backStackEntry.arguments?.getString("sensorId") ?: ""
            SensorDetailScreen(sensorId = sensorId, onBack = { navController.popBackStack() })
        }

        composable(
            route = "credit_detail/{creditId}",
            arguments = listOf(navArgument("creditId") { type = NavType.StringType })
        ) { backStackEntry ->
            val creditId = backStackEntry.arguments?.getString("creditId") ?: ""
            CreditDetailScreen(creditId = creditId, onBack = { navController.popBackStack() })
        }

        composable("anomaly_log") {
            AnomalyLogScreen(
                onViewSensor = { sensorId -> navController.navigate("sensor_detail/$sensorId") }
            )
        }

        composable("notifications") { NotificationsScreen() }
        composable("profile") { ProfileScreen() }
    }
}
