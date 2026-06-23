package com.carboncredit.app.ui.manager.credits

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.core.utils.truncateMiddle
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditDetailScreen(
    creditId: String,
    onBack: () -> Unit,
    viewModel: CreditDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(creditId) { viewModel.loadCredit(creditId) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        TopAppBar(
            title = { Text("Credit Detail", color = TextPrimary) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
        )

        when {
            state.isLoading -> ShimmerList(count = 3)
            state.error != null -> ErrorState(state.error!!, onRetry = { viewModel.loadCredit(creditId) })
            state.credit != null -> {
                val credit = state.credit!!
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                    // Emission Summary
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Emission Summary", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            DetailRow("Period", DateUtils.formatPeriodRange(credit.periodStart, credit.periodEnd))
                            credit.periodId?.let { DetailRow("Period ID", it) }
                            DetailRow("Actual Emissions", "${credit.actualEmissions.formatTwoDecimals()} tonnes")
                            DetailRow("Baseline", "${credit.baselineUsed.formatTwoDecimals()} tonnes")
                            DetailRow("Reduction", "${credit.emissionReduction.formatTwoDecimals()} tonnes")
                            Divider(color = TextTertiary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))

                            // Quality-adjusted credits breakdown
                            if (credit.creditsRaw != null && credit.qualityFactor != null) {
                                DetailRow("Raw credits earned", "${credit.creditsRaw.formatTwoDecimals()} t")
                                Spacer(modifier = Modifier.height(8.dp))

                                // Quality score row
                                val qPercent = (credit.qualityFactor * 100).toInt()
                                val qColor = when {
                                    credit.qualityFactor >= 0.9f -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                    credit.qualityFactor >= 0.7f -> androidx.compose.ui.graphics.Color(0xFFFFC107)
                                    else -> androidx.compose.ui.graphics.Color(0xFFF44336)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Data quality score", color = TextSecondary, fontSize = 13.sp)
                                    Text("q = ${credit.qualityFactor.formatTwoDecimals()} ($qPercent%)", color = qColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { credit.qualityFactor },
                                    modifier = Modifier.fillMaxWidth().height(6.dp),
                                    color = qColor,
                                    trackColor = TextTertiary.copy(alpha = 0.2f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Adjusted credits issued", fontWeight = FontWeight.Bold, color = BlockchainGold, fontSize = 16.sp)
                                Text("${credit.creditsIssued.formatTwoDecimals()}", fontWeight = FontWeight.Bold, color = BlockchainGold, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // IPFS Report
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("📄 IPFS Report", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("This report is securely stored on the decentralized IPFS network, ensuring immutability.", color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("CID: ", color = TextSecondary, fontSize = 13.sp)
                                Text(credit.ipfsCid.truncateMiddle(24), fontFamily = FontFamily.Monospace, color = BlockchainGold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    clip.setPrimaryClip(android.content.ClipData.newPlainText("IPFS CID", credit.ipfsCid))
                                }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    val url = "https://gateway.pinata.cloud/ipfs/${credit.ipfsCid}"
                                    context.startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("View Raw Report") }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Blockchain Record
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("⛓️ Blockchain Record", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("TX: ", color = TextSecondary, fontSize = 13.sp)
                                Text(credit.txHash.truncateMiddle(20), fontFamily = FontFamily.Monospace, color = BlockchainGold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clip.setPrimaryClip(ClipData.newPlainText("TX Hash", credit.txHash))
                                }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                                }
                            }
                            if (credit.blockNumber != null) {
                                Text("Block: ${credit.blockNumber}", color = TextSecondary, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    val url = "${Constants.POLYGONSCAN_BASE_URL}${credit.txHash}"
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Verify on PolygonScan", color = BlueLight) }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // ── On-Chain Contract Record ─────────────────────────────
                    OnChainContractCard(credit = credit, context = context)
                    Spacer(modifier = Modifier.height(16.dp))

                    // QR Code
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍 Verification QR", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            val qrUrl = "${Constants.POLYGONSCAN_BASE_URL}${credit.txHash}"
                            val qrBitmap = remember(credit.txHash) { generateQR(qrUrl) }
                            qrBitmap?.let {
                                Image(bitmap = it.asImageBitmap(), contentDescription = "QR Code", modifier = Modifier.size(180.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Scan to verify on blockchain", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "${Constants.POLYGONSCAN_BASE_URL}${credit.txHash}")
                                }
                                context.startActivity(Intent.createChooser(intent, "Share QR"))
                            }) { Text("Share QR", color = BlueLight) }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Text(value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DetailRowMonospace(label: String, value: String, context: android.content.Context, copyLabel: String = label) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.width(100.dp))
        Text(
            text = value,
            color = BlockchainGold,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                val clip = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clip.setPrimaryClip(android.content.ClipData.newPlainText(copyLabel, value))
            },
            modifier = Modifier.size(28.dp)
        ) {
            Icon(Icons.Default.ContentCopy, null, tint = TextTertiary, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun OnChainContractCard(credit: com.carboncredit.app.data.models.CarbonCredit, context: android.content.Context) {
    // Convert stored gram values (×1e6) back to tonnes for display
    val creditsAdjTonnes = credit.creditsIssued          // already in tonnes from repository
    val totalEmissionsTonnes = credit.actualEmissions
    val emissionReductionTonnes = credit.emissionReduction

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(
                        androidx.compose.ui.graphics.Color(0xFF1C1800),
                        SurfaceCard
                    )
                )
            )
            .border(1.dp, BlockchainGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Header ────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccountTree,
                    contentDescription = null,
                    tint = BlockchainGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        "Smart Contract Record",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                    Text(
                        "CarbonCreditRegistry · CreditRecord struct",
                        color = BlockchainGold.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BlockchainGold.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // ── Field: facilityId ─────────────────────────────────────
            ContractFieldLabel("facilityId")
            Text(
                credit.facilityId,
                color = TextPrimary,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // ── Field: periodId ───────────────────────────────────────
            credit.periodId?.let { pid ->
                ContractFieldLabel("periodId")
                Text(pid, color = TextPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp))
            }

            // ── Field: creditsAdj (×1e6 grams → tonnes) ──────────────
            ContractFieldLabel("creditsAdj  ×1e6 g → tonnes")
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${creditsAdjTonnes.formatTwoDecimals()} tonnes CO₂", color = BlockchainGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("= ${(creditsAdjTonnes * 1_000_000).toLong()} g", color = TextTertiary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }

            // ── Field: totalEmissions (E_total) ───────────────────────
            ContractFieldLabel("totalEmissions  (E_total ×1e6)")
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${totalEmissionsTonnes.formatTwoDecimals()} tonnes", color = TextPrimary, fontSize = 13.sp)
                Text("= ${(totalEmissionsTonnes * 1_000_000).toLong()} g", color = TextTertiary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }

            // ── Field: emissionReduction (E_red) ──────────────────────
            ContractFieldLabel("emissionReduction  (E_red ×1e6)")
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${emissionReductionTonnes.formatTwoDecimals()} tonnes", color = GreenLight, fontSize = 13.sp)
                Text("= ${(emissionReductionTonnes * 1_000_000).toLong()} g", color = TextTertiary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }

            // ── Field: reportHash (bytes32) ───────────────────────────
            credit.reportHash?.let { hash ->
                HorizontalDivider(color = BlockchainGold.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 6.dp))
                DetailRowMonospace(
                    label = "reportHash",
                    value = hash.truncateMiddle(28),
                    context = context,
                    copyLabel = "Report Hash"
                )
            }

            // ── Field: ipfsCid ────────────────────────────────────────
            if (credit.ipfsCid.isNotBlank()) {
                DetailRowMonospace(
                    label = "ipfsCid",
                    value = credit.ipfsCid.truncateMiddle(28),
                    context = context,
                    copyLabel = "IPFS CID"
                )
            }

            // ── Field: timestamp ──────────────────────────────────────
            HorizontalDivider(color = BlockchainGold.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 6.dp))
            DetailRow("timestamp", DateUtils.formatShortDate(credit.createdAt))
            credit.blockNumber?.let { bn ->
                DetailRow("blockNumber", "#$bn")
            }
        }
    }
}

@Composable
private fun ContractFieldLabel(name: String) {
    Text(
        text = name,
        color = BlockchainGold.copy(alpha = 0.7f),
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

private fun generateQR(text: String): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val matrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (_: Exception) { null }
}
