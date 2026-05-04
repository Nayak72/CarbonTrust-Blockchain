package com.carboncredit.app.ui.auditor.facility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.ui.auditor.dashboard.AuditorDashboardViewModel
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun FacilityListScreen(
    onFacilityClick: (String) -> Unit,
    viewModel: AuditorDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text("Facilities", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.padding(16.dp))

        when {
            state.isLoading -> ShimmerList()
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadDashboard() })
            state.facilityStatuses.isEmpty() -> EmptyState("No assigned facilities")
            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.facilityStatuses) { fs ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { onFacilityClick(fs.facility.id) }, colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Business, null, tint = BlueLight, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(fs.facility.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                                    Text(fs.facility.companyName, color = TextSecondary, fontSize = 13.sp)
                                    fs.facility.location?.let { Text(it, color = TextTertiary, fontSize = 12.sp) }
                                }
                                ComplianceBadge(fs.compliance)
                            }
                        }
                    }
                }
            }
        }
    }
}
