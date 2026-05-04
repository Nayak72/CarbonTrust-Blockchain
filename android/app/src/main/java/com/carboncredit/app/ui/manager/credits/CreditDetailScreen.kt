package com.carboncredit.app.ui.manager.credits

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
                            DetailRow("Actual Emissions", "${credit.actualEmissions.formatTwoDecimals()} tonnes")
                            DetailRow("Baseline", "${credit.baselineUsed.formatTwoDecimals()} tonnes")
                            DetailRow("Reduction", "${credit.emissionReduction.formatTwoDecimals()} tonnes")
                            Divider(color = TextTertiary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Credits Issued", fontWeight = FontWeight.Bold, color = BlockchainGold, fontSize = 16.sp)
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
                            Text(credit.ipfsCid, fontFamily = FontFamily.Monospace, color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    val url = "https://gateway.pinata.cloud/ipfs/${credit.ipfsCid}"
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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

                    // QR Code
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍 Verification QR", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            val qrBitmap = remember(credit.txHash) { generateQR(credit.txHash) }
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
