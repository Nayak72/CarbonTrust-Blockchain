package com.carboncredit.app.ui.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carboncredit.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

sealed class ManagerBottomNav(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : ManagerBottomNav("manager_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Sensors : ManagerBottomNav("manager_sensors", "Sensors", Icons.Default.Sensors)
    object Analytics : ManagerBottomNav("manager_analytics", "Analytics", Icons.Default.Analytics)
    object Credits : ManagerBottomNav("manager_credits", "Credits", Icons.Default.AccountBalanceWallet)
    object More : ManagerBottomNav("manager_more", "More", Icons.Default.MoreHoriz)
}

@AndroidEntryPoint
class ManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCreditTheme {
                ManagerMainScreen()
            }
        }
    }
}

@Composable
fun ManagerMainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        ManagerBottomNav.Dashboard, ManagerBottomNav.Sensors,
        ManagerBottomNav.Analytics, ManagerBottomNav.Credits, ManagerBottomNav.More
    )

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark, contentColor = TextPrimary) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BluePrimary,
                            selectedTextColor = BluePrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = BluePrimary.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        ManagerNavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}
