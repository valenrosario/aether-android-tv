package com.aether.tv.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

object AetherColors {
    val Black = Color(0xFF07080C)
    val Surface = Color(0xFF10121A)
    val SurfaceHigh = Color(0xFF191C28)
    val Violet = Color(0xFF8B7CFF)
    val Halo = Color(0xFFE9E6FF)
    val Muted = Color(0xFF8A8FA3)

    /** Paleta clara (liquid glass pastel). */
    val Ink = Color(0xFF1E1740)
    val InkSoft = Color(0xFF5B5480)
    val Accent = Color(0xFF7C5CFF)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AetherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = AetherColors.Accent,
            onPrimary = Color.White,
            background = AetherColors.Black,
            onBackground = AetherColors.Halo,
            surface = AetherColors.Surface,
            onSurface = AetherColors.Halo,
        ),
        content = content,
    )
}
