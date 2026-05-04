package com.carboncredit.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = TextPrimary,
    primaryContainer = BlueLight,
    secondary = GreenLight,
    onSecondary = TextPrimary,
    secondaryContainer = GreenCompliant,
    tertiary = BlockchainGold,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    error = RedCritical,
    onError = TextPrimary,
    outline = TextTertiary
)

@Composable
fun CarbonCreditTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = CarbonTypography,
        shapes = CarbonShapes,
        content = content
    )
}
