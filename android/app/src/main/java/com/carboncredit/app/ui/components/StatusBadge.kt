package com.carboncredit.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carboncredit.app.ui.theme.*

@Composable
fun StatusBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(targetValue = color, label = "badge_color")
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(animatedColor.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = animatedColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SensorStatusBadge(status: String, modifier: Modifier = Modifier) {
    val (text, color) = when (status.uppercase()) {
        "NORMAL" -> "Normal" to SensorNormal
        "WARNING" -> "Warning" to SensorWarning
        "ANOMALY" -> "Anomaly" to SensorAnomaly
        "OFFLINE" -> "Offline" to SensorOffline
        else -> status to TextSecondary
    }
    StatusBadge(text = text, color = color, modifier = modifier)
}

@Composable
fun CreditStatusBadge(status: String, modifier: Modifier = Modifier) {
    val (text, color) = when (status.lowercase()) {
        "verified" -> "✅ Verified" to GreenLight
        "pending" -> "⏳ Pending" to YellowWatch
        "disputed" -> "⚠️ Disputed" to RedCritical
        else -> status to TextSecondary
    }
    StatusBadge(text = text, color = color, modifier = modifier)
}

@Composable
fun ComplianceBadge(status: String, modifier: Modifier = Modifier) {
    val (text, color) = when (status.uppercase()) {
        "COMPLIANT" -> "Compliant" to GreenCompliant
        "WATCH" -> "Watch" to YellowWatch
        "NON-COMPLIANT" -> "Non-Compliant" to RedCritical
        else -> status to TextSecondary
    }
    StatusBadge(text = text, color = color, modifier = modifier)
}

@Composable
fun AnomalyTypeBadge(type: String, modifier: Modifier = Modifier) {
    val (text, color) = when (type) {
        "zscore_breach" -> "Z-Score" to Color(0xFF7B1FA2)
        "frozen_value" -> "Frozen" to Color(0xFF0277BD)
        "zero_reading" -> "Zero" to RedCritical
        "connectivity_gap" -> "Connectivity" to SensorOffline
        else -> type to TextSecondary
    }
    StatusBadge(text = text, color = color, modifier = modifier)
}
