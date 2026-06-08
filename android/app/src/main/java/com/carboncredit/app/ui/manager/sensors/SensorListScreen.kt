package com.carboncredit.app.ui.manager.sensors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
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
import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.data.models.SensorReading
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun SensorListScreen(
    onSensorClick: (String) -> Unit,
    viewModel: SensorListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text(
            "Sensors",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(16.dp)
        )

        when {
            state.isLoading -> ShimmerList()
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadSensors() })
            state.sensors.isEmpty() -> EmptyState("No sensors registered", "Sensors will appear here once configured")
            else -> {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    items(state.sensors) { (sensor, reading) ->
                        SensorCard(sensor, reading, onClick = { onSensorClick(sensor.id) })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SensorCard(sensor: Sensor, reading: SensorReading?, onClick: () -> Unit) {
    val statusColor = when {
        !sensor.isActive -> SensorOffline
        reading?.isAnomaly == true -> SensorAnomaly
        reading != null && reading.co2Ppm > 900 -> SensorWarning
        else -> SensorNormal
    }
    val statusText = when {
        !sensor.isActive -> "OFFLINE"
        reading?.isAnomaly == true -> "ANOMALY"
        reading != null && reading.co2Ppm > 900 -> "WARNING"
        else -> "NORMAL"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(statusColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Sensors, contentDescription = null, tint = statusColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(sensor.locationLabel ?: "Sensor", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                Text(sensor.deviceId.take(12) + "...", color = TextSecondary, fontSize = 12.sp)
                if (sensor.lastSeen != null) {
                    Text("Last seen ${DateUtils.formatRelativeTime(sensor.lastSeen)}", color = TextTertiary, fontSize = 11.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (reading != null) {
                    Text("${reading.co2Ppm.toInt()}", fontWeight = FontWeight.Bold, color = statusColor, fontSize = 20.sp)
                    Text("ppm", color = TextSecondary, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                SensorStatusBadge(statusText)
            }
        }
    }
}
