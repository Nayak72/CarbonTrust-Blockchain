package com.carboncredit.app.ui.auditor.verification

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.core.utils.truncateMiddle
import com.carboncredit.app.data.repository.VerificationResult
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun BlockchainVerificationScreen(viewModel: BlockchainVerificationViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Text("Blockchain Verification", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.padding(16.dp))

        if (state.selectedCredit == null) {
            // Search mode
            OutlinedTextField(
                value = state.searchQuery, onValueChange = { viewModel.search(it) },
                label = { Text("Enter TX Hash or Credit ID") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BlockchainGold, unfocusedBorderColor = TextTertiary, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, cursorColor = BlockchainGold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (state.isSearching) { ShimmerList(count = 2) }
            else if (state.searchResults.isEmpty() && state.searchQuery.length > 3) {
                EmptyState("No results found")
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.searchResults) { credit ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { viewModel.selectCredit(credit) }, colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Link, null, tint = BlockchainGold, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${credit.creditsIssued.formatTwoDecimals()} credits", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    Text("TX: ${credit.txHash.truncateMiddle(18)}", fontFamily = FontFamily.Monospace, color = TextSecondary, fontSize = 12.sp)
                                    Text(DateUtils.formatShortDate(credit.createdAt), color = TextTertiary, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.ChevronRight, null, tint = TextSecondary)
                            }
                        }
                    }
                }
            }
        } else {
            // Verification detail mode
            val credit = state.selectedCredit!!
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    TextButton(onClick = { viewModel.clearSelection() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = BlueLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Back to search", color = BlueLight)
                    }
                }

                // On-chain data
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("⛓️ On-Chain Record", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            MonoRow("TX Hash", credit.txHash.truncateMiddle(24))
                            MonoRow("IPFS CID", credit.ipfsCid.truncateMiddle(24))
                            MonoRow("Credits", credit.creditsIssued.formatTwoDecimals())
                            credit.blockNumber?.let { MonoRow("Block", "$it") }
                        }
                    }
                }

                // Verify button
                item {
                    Button(
                        onClick = { viewModel.verifyIntegrity() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        enabled = !state.isVerifying,
                        colors = ButtonDefaults.buttonColors(containerColor = BlockchainGold)
                    ) {
                        if (state.isVerifying) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = BackgroundDark, strokeWidth = 2.dp)
                        else { Icon(Icons.Default.VerifiedUser, null, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("VERIFY REPORT INTEGRITY", fontWeight = FontWeight.Bold) }
                    }
                }

                // Verification steps
                state.verificationState?.let { vs ->
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                for (i in 1..vs.totalSteps) {
                                    val done = i < vs.step
                                    val current = i == vs.step
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                        if (done) Icon(Icons.Default.CheckCircle, null, tint = GreenLight, modifier = Modifier.size(20.dp))
                                        else if (current) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = BlueLight)
                                        else Icon(Icons.Default.RadioButtonUnchecked, null, tint = TextTertiary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        val label = when (i) { 1 -> "Fetching from Polygon blockchain..."; 2 -> "Fetching report from IPFS..."; 3 -> "Computing SHA-256 hash..."; else -> "Comparing hashes..." }
                                        Text(label, color = if (current) TextPrimary else TextSecondary, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Result
                state.verificationResult?.let { result ->
                    item {
                        val (icon, color, text) = when (result) {
                            VerificationResult.INTACT -> Triple(Icons.Default.Verified, GreenLight, "REPORT INTACT — Hashes match. This emission report has not been tampered with.")
                            VerificationResult.TAMPERED -> Triple(Icons.Default.Error, RedCritical, "HASH MISMATCH — The report does not match the blockchain record. Possible tampering.")
                            VerificationResult.ERROR -> Triple(Icons.Default.ErrorOutline, YellowWatch, "Verification encountered an error. Please try again.")
                        }
                        Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)), shape = RoundedCornerShape(12.dp)) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text, color = color, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Text(value, fontFamily = FontFamily.Monospace, color = TextPrimary, fontSize = 12.sp)
    }
}
