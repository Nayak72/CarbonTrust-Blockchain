package com.carboncredit.app.ui.auditor

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

sealed class AuditorBottomNav(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : AuditorBottomNav("auditor_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Facilities : AuditorBottomNav("auditor_facilities", "Facilities", Icons.Default.Business)
    object Verify : AuditorBottomNav("auditor_verify", "Verify", Icons.Default.VerifiedUser)
    object Compare : AuditorBottomNav("auditor_compare", "Compare", Icons.Default.CompareArrows)
    object More : AuditorBottomNav("auditor_more", "More", Icons.Default.MoreHoriz)
}

@AndroidEntryPoint
class AuditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CarbonCreditTheme { AuditorMainScreen() } }
    }
}

@Composable
fun AuditorMainScreen() {
    val navController = rememberNavController()
    val items = listOf(AuditorBottomNav.Dashboard, AuditorBottomNav.Facilities, AuditorBottomNav.Verify, AuditorBottomNav.Compare, AuditorBottomNav.More)

    Scaffold(containerColor = BackgroundDark, bottomBar = {
        NavigationBar(containerColor = SurfaceDark, contentColor = TextPrimary) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDest = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                    selected = currentDest?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = BlockchainGold, selectedTextColor = BlockchainGold, unselectedIconColor = TextSecondary, unselectedTextColor = TextSecondary, indicatorColor = BlockchainGold.copy(alpha = 0.12f))
                )
            }
        }
    }) { padding ->
        AuditorNavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}
