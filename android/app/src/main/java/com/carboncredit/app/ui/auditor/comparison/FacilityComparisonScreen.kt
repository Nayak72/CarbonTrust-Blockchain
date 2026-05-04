package com.carboncredit.app.ui.auditor.comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun FacilityComparisonScreen(viewModel: FacilityComparisonViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Facility Comparison", style = MaterialTheme.typography.headlineMedium, color = TextPrimary) }
        item { Text("Select up to 4 facilities", color = TextSecondary, fontSize = 13.sp) }

        // Facility selector
        items(state.allFacilities) { facility ->
            val selected = state.selectedIds.contains(facility.id)
            FilterChip(
                selected = selected,
                onClick = { viewModel.toggleFacility(facility.id) },
                label = { Text("${facility.name} — ${facility.companyName}") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BlockchainGold.copy(alpha = 0.2f), selectedLabelColor = BlockchainGold)
            )
        }

        if (state.selectedIds.size >= 2) {
            item {
                Button(onClick = { viewModel.compare() }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), modifier = Modifier.fillMaxWidth()) {
                    Text("Compare Selected (${state.selectedIds.size})")
                }
            }
        }

        // Outlier warning
        state.outlierWarning?.let { warning ->
            item {
                Card(colors = CardDefaults.cardColors(containerColor = YellowWatch.copy(alpha = 0.1f)), shape = RoundedCornerShape(12.dp)) {
                    Text(warning, color = YellowWatch, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(14.dp))
                }
            }
        }

        // Comparison table
        if (state.comparisonData.isNotEmpty()) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Header
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Facility", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1.5f))
                            Text("Emissions", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Credits", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Anomalies", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Status", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
                        }
                        Divider(color = TextTertiary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 6.dp))
                        state.comparisonData.forEach { fs ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Text(fs.facility.name, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                                Text("${fs.actualEmissions.formatTwoDecimals()} t", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                Text(fs.monthCredits.formatTwoDecimals(), color = BlockchainGold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                Text("${fs.unacknowledgedAnomalies}", color = if (fs.unacknowledgedAnomalies > 0) RedCritical else TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                ComplianceBadge(fs.compliance, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
