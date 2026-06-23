package com.carboncredit.app.ui.shared.chain

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.formatTwoDecimals
import com.carboncredit.app.core.utils.truncateMiddle
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.ui.components.EmptyState
import com.carboncredit.app.ui.components.ErrorState
import com.carboncredit.app.ui.components.ShimmerList
import com.carboncredit.app.ui.manager.credits.CreditLedgerViewModel
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChainVisualizerScreen(
    onBack: () -> Unit,
    onBlockClick: (String) -> Unit,
    viewModel: CreditLedgerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        tint = BlockchainGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chain Explorer", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
        )

        // Live chain count header
        if (state.credits.isNotEmpty()) {
            ChainHeaderBanner(blockCount = state.credits.size)
        }

        when {
            state.isLoading -> ShimmerList(count = 5)
            state.error != null -> ErrorState(
                message = state.error!!,
                onRetry = { viewModel.selectFilter("all") }
            )
            state.credits.isEmpty() -> EmptyState("No blocks recorded yet")
            else -> {
                // Show newest block at the top (reverse chronological = most recent block is highest)
                val sortedCredits = state.credits.sortedByDescending { it.createdAt }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(sortedCredits) { index, credit ->
                        BlockNode(
                            credit = credit,
                            blockIndex = sortedCredits.size - index, // highest block number at top
                            isFirst = index == 0,
                            isLast = index == sortedCredits.lastIndex,
                            onClick = { onBlockClick(credit.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ChainHeaderBanner(blockCount: Int) {
    // Animated pulsing gold glow on the border
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF1A1600),
                        Color(0xFF2A2000),
                        Color(0xFF1A1600)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = BlockchainGold.copy(alpha = glowAlpha),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "POLYGON AMOY TESTNET",
                    color = BlockchainGold.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "$blockCount Credit Blocks On-Chain",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            // Live dot indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GreenLight.copy(alpha = glowAlpha))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("LIVE", color = GreenLight, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
private fun BlockNode(
    credit: CarbonCredit,
    blockIndex: Int,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val chainLineColor = BlockchainGold.copy(alpha = 0.5f)
    val nodeColor = BlockchainGold

    Row(modifier = Modifier.fillMaxWidth()) {
        // Left: Chain spine (vertical line + circle node)
        Box(
            modifier = Modifier
                .width(36.dp)
                .fillMaxHeight()
        ) {
            // Vertical connecting line (draw full height, except for first/last nodes)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(2.dp)
                    .fillMaxHeight()
                    .drawBehind {
                        val topY = if (isFirst) size.height / 2f else 0f
                        val bottomY = if (isLast) size.height / 2f else size.height
                        drawLine(
                            color = chainLineColor,
                            start = Offset(size.width / 2f, topY),
                            end = Offset(size.width / 2f, bottomY),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
            )

            // Circle node on the spine
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(BackgroundDark)
                    .border(2.dp, nodeColor, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right: The Block Card
        BlockCard(
            credit = credit,
            blockIndex = blockIndex,
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun BlockCard(
    credit: CarbonCredit,
    blockIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Block Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Block number badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(BlockchainGold.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Block #$blockIndex",
                        color = BlockchainGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Verified badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = GreenLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = credit.status.uppercase(),
                        color = GreenLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = TextTertiary.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(10.dp))

            // Credit value - prominent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("Credits Issued", color = TextSecondary, fontSize = 11.sp)
                    Text(
                        text = "${credit.creditsIssued.formatTwoDecimals()} t CO₂",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Reduction", color = TextSecondary, fontSize = 11.sp)
                    Text(
                        text = "${credit.emissionReduction.formatTwoDecimals()} t",
                        color = GreenLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hash row
            if (credit.txHash.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        tint = BlockchainGold.copy(alpha = 0.7f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = credit.txHash.truncateMiddle(22),
                        color = BlockchainGold.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Block number on-chain + timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                credit.blockNumber?.let {
                    Text(
                        text = "Blk: $it",
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = DateUtils.formatShortDate(credit.createdAt),
                    color = TextTertiary,
                    fontSize = 10.sp
                )
            }

            // Quality factor bar (if present)
            credit.qualityFactor?.let { qf ->
                Spacer(modifier = Modifier.height(10.dp))
                val qColor = when {
                    qf >= 0.9f -> GreenLight
                    qf >= 0.7f -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Data Quality", color = TextTertiary, fontSize = 10.sp)
                    Text(
                        text = "${(qf * 100).toInt()}%",
                        color = qColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { qf },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = qColor,
                    trackColor = TextTertiary.copy(alpha = 0.15f)
                )
            }
        }
    }
}
