package com.carboncredit.app.ui.auditor.facility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilityDetailScreen(
    facilityId: String,
    onBack: () -> Unit,
    viewModel: FacilityDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(facilityId) { viewModel.loadFacility(facilityId) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        TopAppBar(title = { Text(state.facility?.name ?: "Facility", color = TextPrimary) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark))

        when {
            state.isLoading -> ShimmerList(count = 4)
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadFacility(facilityId) })
            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Compliance banner
                    item {
                        val bannerColor = when (state.compliance) {
                            "COMPLIANT" -> GreenCompliant; "WATCH" -> YellowWatch; else -> RedCritical
                        }
                        Card(colors = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.15f)), shape = RoundedCornerShape(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(state.compliance, fontWeight = FontWeight.Bold, color = bannerColor, fontSize = 16.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("Baseline: ${state.facility?.baselineEmissions?.formatTwoDecimals()} t/mo", color = TextSecondary, fontSize = 12.sp)
                            }
                        }
                    }

                    // Info
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                InfoRow("Company", state.facility?.companyName ?: "")
                                InfoRow("Location", state.facility?.location ?: "N/A")
                                InfoRow("Industry", state.facility?.industryType ?: "N/A")
                                InfoRow("Total Emissions (30d)", "${state.totalEmissions.formatTwoDecimals()} tonnes")
                            }
                        }
                    }

                    // Sensors
                    item { Text("Sensor Health", style = MaterialTheme.typography.titleMedium, color = TextPrimary) }
                    if (state.sensors.isEmpty()) { item { EmptyState("No sensors") } }
                    items(state.sensors) { sensor ->
                        Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(10.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Sensors, null, tint = if (sensor.isActive) SensorNormal else SensorOffline, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(sensor.locationLabel ?: sensor.deviceId, color = TextPrimary, fontSize = 14.sp)
                                    sensor.lastSeen?.let { Text("Last: ${DateUtils.formatRelativeTime(it)}", color = TextTertiary, fontSize = 11.sp) }
                                }
                                SensorStatusBadge(if (sensor.isActive) "NORMAL" else "OFFLINE")
                            }
                        }
                    }

                    // Anomaly history
                    item { Spacer(modifier = Modifier.height(4.dp)); Text("Anomaly History (${state.anomalies.size})", style = MaterialTheme.typography.titleMedium, color = TextPrimary) }
                    items(state.anomalies.take(20)) { anomaly ->
                        Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(10.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = RedCritical, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${anomaly.co2Value} ppm — ${anomaly.anomalyType}", color = TextPrimary, fontSize = 13.sp)
                                    Text(DateUtils.formatDisplay(anomaly.timestamp), color = TextTertiary, fontSize = 11.sp)
                                }
                                if (anomaly.isAcknowledged) StatusBadge("ACK", GreenLight) else StatusBadge("PENDING", YellowWatch)
                            }
                        }
                    }

                    // Credits
                    item { Spacer(modifier = Modifier.height(4.dp)); Text("Credit History (${state.credits.size})", style = MaterialTheme.typography.titleMedium, color = TextPrimary) }
                    items(state.credits.take(10)) { credit ->
                        Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(10.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, null, tint = BlockchainGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${credit.creditsIssued.formatTwoDecimals()} credits", fontWeight = FontWeight.Medium, color = TextPrimary, fontSize = 14.sp)
                                    Text(DateUtils.formatPeriodRange(credit.periodStart, credit.periodEnd), color = TextTertiary, fontSize = 11.sp)
                                }
                                CreditStatusBadge(credit.status)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Text(value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
