package com.example.yazumi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val YazumiColorScheme = lightColorScheme(
    primary = YazumiRed,
    onPrimary = Color.White,
    primaryContainer = YazumiYellow,
    onPrimaryContainer = YazumiTextPrimary,
    secondary = YazumiBlue,
    onSecondary = Color.White,
    secondaryContainer = YazumiBlueLight,
    onSecondaryContainer = Color.White,
    tertiary = YazumiYellowDark,
    onTertiary = YazumiTextPrimary,
    background = YazumiBackground,
    onBackground = YazumiTextPrimary,
    surface = YazumiSurface,
    onSurface = YazumiTextPrimary,
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = YazumiTextSecondary,
    error = Color(0xFFB00020),
    onError = Color.White,
)

@Composable
fun YazumiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = YazumiColorScheme,
        typography = Typography,
        content = content,
    )
}
