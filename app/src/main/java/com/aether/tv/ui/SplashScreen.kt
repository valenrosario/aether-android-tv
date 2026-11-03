package com.aether.tv.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val draw = remember { Animatable(0f) }
    val text = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        draw.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
        text.animateTo(1f, tween(500))
        kotlinx.coroutines.delay(500)
        onFinished()
    }
    Column(
        Modifier.fillMaxSize().background(AetherColors.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AetherMark(120.dp, progress = draw.value)
        Text(
            "AETHER",
            color = AetherColors.Halo,
            fontSize = 34.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 10.sp,
            modifier = Modifier.alpha(text.value),
        )
    }
}
