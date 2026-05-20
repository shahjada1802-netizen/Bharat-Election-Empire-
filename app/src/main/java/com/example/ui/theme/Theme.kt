package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    secondary = AshokaBlue,
    tertiary = EmeraldGreen,
    background = DeepCharcoalBg,      // Warm Peach canvas
    surface = SurfaceCardNavy,       // Pure white rounded card
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextWhiteRegular, // High contrast text (#1D1B1E)
    onSurface = TextWhiteRegular
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    secondary = AshokaBlue,
    tertiary = EmeraldGreen,
    background = DeepCharcoalBg,      // Warm Peach canvas
    surface = SurfaceCardNavy,       // Pure white rounded card
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextWhiteRegular, // High contrast text (#1D1B1E)
    onSurface = TextWhiteRegular
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Keep it always matched to the "Vibrant Palette" styling
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
