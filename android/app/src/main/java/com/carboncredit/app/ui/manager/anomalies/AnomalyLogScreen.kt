package com.carboncredit.app.ui.manager.anomalies

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
import com.carboncredit.app.data.models.AnomalyEvent
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun AnomalyLogScreen(
    onViewSensor: (String) -> Unit = {},
    viewModel: AnomalyLogViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var expandedAnomalyId by remember { mutableStateOf<String?>(null) }
    var ackNote by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text("Anomaly Log", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.padding(16.dp))

        // Filter chips
        val types = listOf(null to "All", "zscore_breach" to "Z-Score", "frozen_value" to "Frozen", "zero_reading" to "Zero", "connectivity_gap" to "Connectivity")
        Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            types.forEach { (key, label) ->
                FilterChip(
                    selected = state.selectedType == key,
                    onClick = { viewModel.filterByType(key) },
                    label = { Text(label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BluePrimary.copy(alpha = 0.2f), selectedLabelColor = BlueLight)
                )
            }
        }

        when {
            state.isLoading -> ShimmerList()
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadAnomalies() })
            state.anomalies.isEmpty() -> EmptyState("No anomalies found", "All readings are within normal range")
            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.anomalies) { anomaly ->
                        AnomalyCard(
                            anomaly = anomaly,
                            isExpanded = expandedAnomalyId == anomaly.id,
                            isAcknowledging = state.acknowledging == anomaly.id,
                            ackNote = if (expandedAnomalyId == anomaly.id) ackNote else "",
                            onToggle = { expandedAnomalyId = if (expandedAnomalyId == anomaly.id) null else anomaly.id; ackNote = "" },
                            onNoteChange = { ackNote = it },
                            onAcknowledge = { viewModel.acknowledgeAnomaly(anomaly.id, ackNote.ifBlank { null }); expandedAnomalyId = null },
                            onViewSensor = { onViewSensor(anomaly.sensorId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnomalyCard(
    anomaly: AnomalyEvent, isExpanded: Boolean, isAcknowledging: Boolean,
    ackNote: String, onToggle: () -> Unit, onNoteChange: (String) -> Unit,
    onAcknowledge: () -> Unit, onViewSensor: () -> Unit
) {
    Card(
        onClick = onToggle,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = RedCritical, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("${anomaly.co2Value} ppm", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                    Text(DateUtils.formatDisplay(anomaly.timestamp), color = TextSecondary, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    AnomalyTypeBadge(anomaly.anomalyType)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (anomaly.isAcknowledged) {
                        StatusBadge("Acknowledged", GreenLight)
                    } else {
                        StatusBadge("Pending", YellowWatch)
                    }
                }
            }

            if (anomaly.zScore != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Z-Score: ${anomaly.zScore}", color = TextTertiary, fontSize = 11.sp)
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = TextTertiary.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                if (anomaly.isAcknowledged) {
                    anomaly.acknowledgementNote?.let {
                        Text("Note: $it", color = TextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    anomaly.acknowledgedAt?.let {
                        Text("Acknowledged ${DateUtils.formatRelativeTime(it)}", color = GreenLight, fontSize = 12.sp)
                    }
                } else {
                    OutlinedTextField(
                        value = ackNote, onValueChange = onNoteChange,
                        label = { Text("Add a note (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = TextTertiary, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, cursorColor = BluePrimary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onAcknowledge, enabled = !isAcknowledging,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenCompliant)
                        ) {
                            if (isAcknowledging) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = TextPrimary)
                            else Text("Acknowledge")
                        }
                        OutlinedButton(onClick = onViewSensor) { Text("View Sensor", color = BlueLight) }
                    }
                }
            }
        }
    }
}
