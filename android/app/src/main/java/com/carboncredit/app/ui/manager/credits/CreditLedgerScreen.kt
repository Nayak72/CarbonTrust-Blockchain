package com.carboncredit.app.ui.manager.credits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
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
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun CreditLedgerScreen(
    onCreditClick: (String) -> Unit,
    viewModel: CreditLedgerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text("Carbon Credit Ledger", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.padding(16.dp))

        // Total credits header
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Credits Earned", color = TextSecondary, fontSize = 13.sp)
                Text("${state.totalCredits.formatTwoDecimals()} tonnes CO₂", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BlockchainGold)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Filters
        val filters = listOf("all" to "All", "month" to "This Month", "year" to "This Year")
        Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEach { (key, label) ->
                FilterChip(
                    selected = state.selectedFilter == key,
                    onClick = { viewModel.selectFilter(key) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BluePrimary.copy(alpha = 0.2f),
                        selectedLabelColor = BlueLight
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.isLoading -> ShimmerList()
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.selectFilter(state.selectedFilter) })
            state.credits.isEmpty() -> EmptyState("No credits issued yet")
            else -> {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    items(state.credits) { credit ->
                        CreditCardItem(credit, onClick = { onCreditClick(credit.id) })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CreditCardItem(credit: CarbonCredit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(DateUtils.formatShortDate(credit.createdAt), color = TextSecondary, fontSize = 12.sp)
                CreditStatusBadge(credit.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("${credit.creditsIssued.formatTwoDecimals()} credits", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
            Text("Reduction: ${credit.emissionReduction.formatTwoDecimals()} tonnes CO₂", color = GreenLight, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(DateUtils.formatPeriodRange(credit.periodStart, credit.periodEnd), color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Link, contentDescription = null, tint = BlockchainGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Blockchain verified", color = BlockchainGold, fontSize = 11.sp)
            }
        }
    }
}
