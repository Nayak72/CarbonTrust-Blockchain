package com.carboncredit.app.ui.manager

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
import com.carboncredit.app.ui.theme.*

@Composable
fun MoreMenuScreen(
    onNotifications: () -> Unit,
    onAnomalyLog: () -> Unit,
    onProfile: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(16.dp)
    ) {
        Text("More", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        MoreMenuItem(icon = Icons.Default.Notifications, title = "Notifications", onClick = onNotifications)
        MoreMenuItem(icon = Icons.Default.Warning, title = "Anomaly Log", onClick = onAnomalyLog)
        MoreMenuItem(icon = Icons.Default.Person, title = "Profile & Settings", onClick = onProfile)
    }
}

@Composable
private fun MoreMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
    }
}
