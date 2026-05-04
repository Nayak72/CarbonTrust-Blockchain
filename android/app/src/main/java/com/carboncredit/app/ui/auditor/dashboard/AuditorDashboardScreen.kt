package com.carboncredit.app.ui.auditor.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun AuditorDashboardScreen(
    onFacilityClick: (String) -> Unit,
    viewModel: AuditorDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> ShimmerList(count = 4)
        state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadDashboard() })
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text("Auditor Dashboard", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                    Text("Welcome, ${state.auditorName}", color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Summary cards
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SummaryCard("Facilities", "${state.totalFacilities}", Icons.Default.Business, BlueLight, Modifier.weight(1f))
                        SummaryCard("Credits", state.totalCreditsMonth.formatTwoDecimals(), Icons.Default.Verified, BlockchainGold, Modifier.weight(1f))
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SummaryCard("Anomalies", "${state.totalActiveAnomalies}", Icons.Default.Warning, RedCritical, Modifier.weight(1f))
                        SummaryCard("Non-Compliant", "${state.nonCompliantCount}", Icons.Default.Error, YellowWatch, Modifier.weight(1f))
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)); Text("Assigned Facilities", style = MaterialTheme.typography.titleMedium, color = TextPrimary) }

                items(state.facilityStatuses) { fs ->
                    FacilityStatusCard(fs, onClick = { onFacilityClick(fs.facility.id) })
                }

                if (state.facilityStatuses.isEmpty()) {
                    item { EmptyState("No assigned facilities") }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 22.sp)
            Text(title, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun FacilityStatusCard(fs: FacilityStatus, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(fs.facility.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                    Text(fs.facility.companyName, color = TextSecondary, fontSize = 13.sp)
                    fs.facility.location?.let { Text(it, color = TextTertiary, fontSize = 12.sp) }
                }
                ComplianceBadge(fs.compliance)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                MiniStat("Credits", fs.monthCredits.formatTwoDecimals(), BlockchainGold)
                MiniStat("Anomalies", "${fs.unacknowledgedAnomalies}", if (fs.unacknowledgedAnomalies > 0) RedCritical else TextSecondary)
                MiniStat("Emissions", "${fs.actualEmissions.formatTwoDecimals()} t", BlueLight)
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
        Text(label, color = TextSecondary, fontSize = 10.sp)
    }
}
