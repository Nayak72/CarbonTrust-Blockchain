package com.carboncredit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carboncredit.app.ui.theme.SurfaceCard
import com.carboncredit.app.ui.theme.SurfaceCardElevated

@Composable
fun ShimmerBox(
    width: Dp = 200.dp,
    height: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            SurfaceCard,
            SurfaceCardElevated,
            SurfaceCard
        ),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
    )
}

@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .padding(16.dp)
    ) {
        ShimmerBox(width = 180.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(12.dp))
        ShimmerBox(width = 260.dp, height = 14.dp)
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerBox(width = 120.dp, height = 14.dp)
    }
}

@Composable
fun ShimmerList(count: Int = 5, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        repeat(count) {
            ShimmerCard()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
