package com.carboncredit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.ui.theme.*

@Composable
fun LiveReadingCard(
    co2Ppm: Float,
    temperature: Float,
    humidity: Float,
    isLive: Boolean = true,
    modifier: Modifier = Modifier
) {
    val co2Color = when {
        co2Ppm < Constants.CO2_NORMAL_MAX -> GreenLight
        co2Ppm < Constants.CO2_WARNING_MAX -> YellowWatch
        else -> RedCritical
    }

    // Pulsing animation for LIVE indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // LIVE indicator
            if (isLive) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(RedCritical)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("LIVE", color = RedCritical, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // CO2 value
            Text(
                text = "%.0f".format(co2Ppm),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = co2Color
            )
            Text(
                text = "ppm CO₂",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = TextTertiary.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Temperature & Humidity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, contentDescription = null, tint = YellowWatch, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("%.1f°C".format(temperature), color = TextPrimary, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = BlueLight, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("%.1f%%".format(humidity), color = TextPrimary, fontSize = 14.sp)
                }
            }
        }
    }
}
