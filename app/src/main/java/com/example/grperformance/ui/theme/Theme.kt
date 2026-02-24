package com.example.grperformance.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = iOSBlue,
    background = iOSBackground,
    surface = iOSCardWhite,
    onBackground = iOSTextBlack,
    onSurface = iOSTextBlack
)

@Composable
fun GRPerformanceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography, // The one we made earlier
        content = content
    )
}