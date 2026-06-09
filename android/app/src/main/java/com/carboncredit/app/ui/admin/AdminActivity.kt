package com.carboncredit.app.ui.admin

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

sealed class AdminBottomNav(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : AdminBottomNav("admin_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Auditors : AdminBottomNav("admin_auditors", "Auditors", Icons.Default.Group)
    object Profile : AdminBottomNav("admin_profile", "Profile", Icons.Default.Person)
}

@AndroidEntryPoint
class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCreditTheme {
                AdminMainScreen()
            }
        }
    }
}

@Composable
fun AdminMainScreen() {
    val navController = rememberNavController()
    val items = listOf(AdminBottomNav.Dashboard, AdminBottomNav.Auditors, AdminBottomNav.Profile)

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
        AdminNavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}
