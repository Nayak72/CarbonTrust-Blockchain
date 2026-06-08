package com.carboncredit.app.ui.manager.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun EmissionAnalyticsScreen(viewModel: EmissionAnalyticsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text("Emission Analytics", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.padding(16.dp))

        // Period selector
        val periods = listOf("today" to "Today", "7days" to "7 Days", "30days" to "30 Days")
        Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            periods.forEach { (key, label) ->
                FilterChip(
                    selected = state.selectedPeriod == key,
                    onClick = { viewModel.selectPeriod(key) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BluePrimary.copy(alpha = 0.2f),
                        selectedLabelColor = BlueLight
                    )
                )
            }
        }

        when {
            state.isLoading -> ShimmerList(count = 3)
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.selectPeriod(state.selectedPeriod) })
            else -> {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                    // Summary stats
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AnalyticsStat("Total CO₂", "${state.totalEmitted.formatTwoDecimals()} t", Icons.Default.Cloud, BlueLight, Modifier.weight(1f))
                        AnalyticsStat("Reduction", "${state.emissionReduction.formatTwoDecimals()} t", Icons.Default.TrendingDown, GreenLight, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AnalyticsStat("Below Baseline", "${state.belowBaselinePct.formatTwoDecimals()}%", Icons.Default.ArrowDownward, GreenCompliant, Modifier.weight(1f))
                        AnalyticsStat("Credits Earned", state.creditsEarned.formatTwoDecimals(), Icons.Default.Verified, BlockchainGold, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Actual vs Baseline visualization
                    Text("Actual vs Baseline", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    EmissionBarComparison(actual = state.totalEmitted, baseline = state.baseline)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Reading distribution
                    if (state.readings.isNotEmpty()) {
                        Text("Reading Distribution", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        ReadingDistribution(state.readings)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsStat(
    label: String, value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier
) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
            Text(label, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun EmissionBarComparison(actual: Float, baseline: Float) {
    val maxVal = maxOf(actual, baseline, 1f)
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            BarRow("Actual", actual, maxVal, BlueLight)
            Spacer(modifier = Modifier.height(12.dp))
            BarRow("Baseline", baseline, maxVal, RedCritical.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun BarRow(label: String, value: Float, max: Float, color: androidx.compose.ui.graphics.Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = TextSecondary, fontSize = 12.sp)
            Text("${value.formatTwoDecimals()} t", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(SurfaceCardElevated, RoundedCornerShape(6.dp))) {
            Box(modifier = Modifier.fillMaxWidth(fraction = (value / max).coerceIn(0f, 1f)).height(12.dp).background(color, RoundedCornerShape(6.dp)))
        }
    }
}

@Composable
private fun ReadingDistribution(readings: List<com.carboncredit.app.data.models.SensorReading>) {
    val normal = readings.count { it.co2Ppm < 600 }
    val warning = readings.count { it.co2Ppm in 600f..900f }
    val critical = readings.count { it.co2Ppm > 900 }
    val total = readings.size.toFloat().coerceAtLeast(1f)

    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            DistRow("Normal (<600 ppm)", normal, total, GreenLight)
            Spacer(modifier = Modifier.height(8.dp))
            DistRow("Warning (600-900 ppm)", warning, total, YellowWatch)
            Spacer(modifier = Modifier.height(8.dp))
            DistRow("Critical (>900 ppm)", critical, total, RedCritical)
        }
    }
}

@Composable
private fun DistRow(label: String, count: Int, total: Float, color: androidx.compose.ui.graphics.Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text("$count", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.width(80.dp).height(8.dp).background(SurfaceCardElevated, RoundedCornerShape(4.dp))) {
            Box(modifier = Modifier.fillMaxWidth(count / total).height(8.dp).background(color, RoundedCornerShape(4.dp)))
        }
    }
}
