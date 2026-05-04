package com.carboncredit.app.ui.manager.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun DashboardScreen(
    onSensorClick: (String) -> Unit,
    onAnomalyClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> ShimmerList(count = 4)
        state.error != null -> ErrorState(message = state.error!!, onRetry = { viewModel.loadDashboard() })
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundDark)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "Welcome back 👋",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = state.facility?.name ?: "Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Live CO2 Card
                state.latestReading?.let { reading ->
                    LiveReadingCard(
                        co2Ppm = reading.co2Ppm,
                        temperature = reading.temperature,
                        humidity = reading.humidity
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Sensor Status Row
                if (state.sensors.isNotEmpty()) {
                    Text("Sensor Status", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.sensors.forEach { sensor ->
                            SensorChip(sensor = sensor, onClick = { onSensorClick(sensor.id) })
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Today vs Baseline
                Text("Today vs Baseline", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                EmissionComparisonCard(
                    todayEmissions = state.todayEmissions,
                    baseline = state.baselineEmissions
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Credit Counters
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CreditCounterCard(
                        title = "Credits Today",
                        value = state.creditsToday.formatTwoDecimals(),
                        icon = Icons.Default.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                    CreditCounterCard(
                        title = "Credits Month",
                        value = state.creditsMonth.formatTwoDecimals(),
                        icon = Icons.Default.CalendarMonth,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Recent Anomalies
                if (state.recentAnomalies.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent Anomalies", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        TextButton(onClick = onAnomalyClick) { Text("View All", color = BlueLight) }
                    }
                    state.recentAnomalies.forEach { anomaly ->
                        AnomalyMiniCard(anomaly)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SensorChip(sensor: Sensor, onClick: () -> Unit) {
    val statusColor = if (sensor.isActive) SensorNormal else SensorOffline
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
            Spacer(modifier = Modifier.width(8.dp))
            Text(sensor.locationLabel ?: sensor.deviceId.take(8), color = TextPrimary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun EmissionComparisonCard(todayEmissions: Float, baseline: Float) {
    val reduction = if (baseline > 0) ((baseline - todayEmissions) / baseline * 100) else 0f
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Today", color = TextSecondary, fontSize = 12.sp)
                    Text("${todayEmissions.formatTwoDecimals()} t", color = BlueLight, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Baseline", color = TextSecondary, fontSize = 12.sp)
                    Text("${baseline.formatTwoDecimals()} t", color = TextSecondary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (reduction > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("📉 ${reduction.formatTwoDecimals()}% below baseline", color = GreenLight, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun CreditCounterCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = BlockchainGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(title, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun AnomalyMiniCard(anomaly: com.carboncredit.app.data.models.AnomalyEvent) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = RedCritical, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("${anomaly.co2Value} ppm", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(DateUtils.formatRelativeTime(anomaly.timestamp), color = TextSecondary, fontSize = 12.sp)
            }
            AnomalyTypeBadge(type = anomaly.anomalyType)
        }
    }
}
