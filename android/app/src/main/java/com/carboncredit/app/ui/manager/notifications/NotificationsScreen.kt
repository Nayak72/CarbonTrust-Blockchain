package com.carboncredit.app.ui.manager.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.Notification
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun NotificationsScreen(viewModel: NotificationsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            TextButton(onClick = { viewModel.markAllRead() }) { Text("Mark All Read", color = BlueLight, fontSize = 13.sp) }
        }

        // Tabs
        val tabs = listOf("all" to "All", "anomaly" to "Anomalies", "credit_issued" to "Credits", "system" to "System")
        ScrollableTabRow(selectedTabIndex = tabs.indexOfFirst { it.first == state.selectedTab }.coerceAtLeast(0), containerColor = BackgroundDark, edgePadding = 16.dp) {
            tabs.forEach { (key, label) ->
                Tab(selected = state.selectedTab == key, onClick = { viewModel.selectTab(key) }, text = { Text(label, color = if (state.selectedTab == key) BlueLight else TextSecondary) })
            }
        }

        when {
            state.isLoading -> ShimmerList()
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadNotifications() })
            state.notifications.isEmpty() -> EmptyState("No notifications", "You're all caught up!")
            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.notifications) { notif ->
                        NotificationCard(notif, onClick = { viewModel.markRead(notif.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(notif: Notification, onClick: () -> Unit) {
    val (icon, color) = when (notif.type) {
        "anomaly" -> Icons.Default.Warning to RedCritical
        "credit_issued" -> Icons.Default.Verified to BlockchainGold
        else -> Icons.Default.Info to BlueLight
    }

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(notif.title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(notif.body, color = TextSecondary, fontSize = 13.sp, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(DateUtils.formatRelativeTime(notif.createdAt), color = TextTertiary, fontSize = 11.sp)
            }
            if (!notif.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BlueLight))
            }
        }
    }
}
