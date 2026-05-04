package com.carboncredit.app.ui.manager.sensors

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorDetailScreen(
    sensorId: String,
    onBack: () -> Unit,
    viewModel: SensorDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(sensorId) { viewModel.loadSensor(sensorId) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        // Top bar
        TopAppBar(
            title = { Text(state.sensor?.locationLabel ?: "Sensor Detail", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            actions = {
                IconButton(onClick = {
                    val csv = viewModel.generateCsv()
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/csv"
                        putExtra(Intent.EXTRA_TEXT, csv)
                        putExtra(Intent.EXTRA_SUBJECT, "Sensor Data Export")
                    }
                    context.startActivity(Intent.createChooser(intent, "Export CSV"))
                }) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Export", tint = BlueLight)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
        )

        // Period selector
        val periods = listOf("today" to "Today", "7days" to "7 Days", "30days" to "30 Days")
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { (key, label) ->
                FilterChip(
                    selected = state.selectedPeriod == key,
                    onClick = { viewModel.selectPeriod(sensorId, key) },
                    label = { Text(label, fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BluePrimary.copy(alpha = 0.2f),
                        selectedLabelColor = BlueLight
                    )
                )
            }
        }

        when {
            state.isLoading -> ShimmerList(count = 3)
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadSensor(sensorId) })
            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Stats card
                    item {
                        StatsCard(state.minPpm, state.maxPpm, state.avgPpm, state.anomalyCount)
                    }

                    // Readings table header
                    item {
                        Text("Recent Readings", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        ReadingTableHeader()
                    }

                    // Readings rows
                    items(state.readings.takeLast(50).reversed()) { reading ->
                        ReadingRow(reading)
                    }

                    if (state.readings.isEmpty()) {
                        item { EmptyState("No readings", "No sensor data for this period") }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(min: Float, max: Float, avg: Float, anomalyCount: Int) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Min", "${min.toInt()} ppm", BlueLight)
            StatItem("Max", "${max.toInt()} ppm", RedCritical)
            StatItem("Avg", "${avg.toInt()} ppm", TextPrimary)
            StatItem("Anomalies", "$anomalyCount", YellowWatch)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)
        Text(label, color = TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun ReadingTableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().background(SurfaceCardElevated).padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text("Time", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1.5f))
        Text("CO₂", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
        Text("Temp", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
        Text("Hum", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
        Text("Status", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ReadingRow(reading: com.carboncredit.app.data.models.SensorReading) {
    val bgColor = if (reading.isAnomaly) RedCritical.copy(alpha = 0.08f) else SurfaceCard
    Row(
        modifier = Modifier.fillMaxWidth().background(bgColor).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(DateUtils.formatDisplay(reading.timestamp).takeLast(8), color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1.5f))
        Text("${reading.co2Ppm.toInt()}", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text("${reading.temperature.formatTwoDecimals()}", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text("${reading.humidity.formatTwoDecimals()}", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1f))
        if (reading.isAnomaly) {
            Text("⚠", color = RedCritical, fontSize = 12.sp, modifier = Modifier.weight(1f))
        } else {
            Text("✓", color = GreenLight, fontSize = 12.sp, modifier = Modifier.weight(1f))
        }
    }
}
