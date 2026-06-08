package com.carboncredit.app.ui.auditor.reports

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun AuditReportScreen(viewModel: AuditReportViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            if (uri != null) {
                val pdfUri = viewModel.getPdfUri(context)
                if (pdfUri != null) {
                    try {
                        context.contentResolver.openInputStream(pdfUri)?.use { input ->
                            context.contentResolver.openOutputStream(uri)?.use { output ->
                                input.copyTo(output)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    )

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Audit Report", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> ShimmerList(count = 2)
            state.error != null -> ErrorState(state.error!!)
            else -> {
                // Facility selector
                Text("Select Facility", color = TextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                state.facilities.forEach { fac ->
                    FilterChip(
                        selected = state.selectedFacilityId == fac.id,
                        onClick = { viewModel.selectFacility(fac.id) },
                        label = { Text(fac.name) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BlockchainGold.copy(alpha = 0.2f), selectedLabelColor = BlockchainGold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Credit Issuance Selector
                if (state.selectedFacilityId != null) {
                    Text("Select Credit Allocation", color = TextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.creditsForSelectedFacility.isEmpty()) {
                        Text("No credit issuances found for this facility.", color = TextSecondary, fontSize = 12.sp)
                    } else {
                        state.creditsForSelectedFacility.forEach { credit ->
                            val dateStr = if (credit.createdAt.length >= 10) credit.createdAt.take(10) else "N/A"
                            FilterChip(
                                selected = state.selectedCreditId == credit.id,
                                onClick = { viewModel.selectCredit(credit.id) },
                                label = { Text("Issuance: ${credit.creditsIssued} credits ($dateStr)") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BluePrimary.copy(alpha = 0.2f), selectedLabelColor = BluePrimary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Generate button
                Button(
                    onClick = { viewModel.generateReport() },
                    enabled = state.selectedFacilityId != null && state.selectedCreditId != null && !state.isGenerating,
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isGenerating) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = TextPrimary)
                    else { Icon(Icons.Default.Description, null, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Generate Report") }
                }

                // Report preview
                if (state.reportReady) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("📄 Report Preview", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.reportSummary, fontFamily = FontFamily.Monospace, color = TextSecondary, fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val fileName = "Audit_Report_${state.selectedCreditId?.take(6) ?: "export"}.pdf"
                            createDocumentLauncher.launch(fileName)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlockchainGold),
                        modifier = Modifier.fillMaxWidth()
                    ) { Icon(Icons.Default.Download, null, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Download PDF") }
                }
            }
        }
    }
}
