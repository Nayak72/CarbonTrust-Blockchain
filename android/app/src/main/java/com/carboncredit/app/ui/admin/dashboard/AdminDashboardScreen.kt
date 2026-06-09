package com.carboncredit.app.ui.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.network.CompanyDashboardData
import com.carboncredit.app.core.network.FacilityDashboardData
import com.carboncredit.app.core.network.AuditorDashboardData
import com.carboncredit.app.ui.components.EmptyState
import com.carboncredit.app.ui.components.ErrorState
import com.carboncredit.app.ui.components.ShimmerList
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> ShimmerList(count = 4)
                state.error != null -> ErrorState(message = state.error!!, onRetry = { viewModel.loadDashboardData() })
                state.companies.isEmpty() -> EmptyState(title = "No Data", subtitle = "No companies or facilities found.")
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(state.companies, key = { it.company_name }) { company ->
                            CompanySection(company = company)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompanySection(company: CompanyDashboardData) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Business, contentDescription = "Company", tint = BluePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = company.company_name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        if (company.facilities.isEmpty()) {
            Text("No facilities under this company.", color = TextSecondary, fontSize = 13.sp)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                company.facilities.forEach { facility ->
                    FacilityCard(facility = facility)
                }
            }
        }
    }
}

@Composable
fun FacilityCard(facility: FacilityDashboardData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Factory, contentDescription = "Facility", tint = BlueLight, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(facility.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(facility.location ?: "Unknown Location", color = TextSecondary, fontSize = 13.sp)
                Text(facility.industry_type ?: "General", color = TextSecondary, fontSize = 13.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = BackgroundDark)
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("Assigned Auditors", fontWeight = FontWeight.Medium, color = TextTertiary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (facility.auditors.isEmpty()) {
                Text("None assigned", color = TextSecondary, fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    facility.auditors.forEach { auditor ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BluePrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    auditor.full_name.firstOrNull()?.uppercase() ?: "A",
                                    color = BlueLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(auditor.full_name, color = TextPrimary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(auditor.email, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
