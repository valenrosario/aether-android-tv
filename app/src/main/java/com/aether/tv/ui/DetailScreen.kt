package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MediaType

@Composable
fun DetailScreen(
    item: MediaItem,
    inList: Boolean,
    onPlay: () -> Unit,
    onToggleList: () -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    val playFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { playFocus.requestFocus() } }
    val night = Color(0xFF0B0A22)

    Box(Modifier.fillMaxSize()) {
        SceneArt(Color(item.colorA), Color(item.colorB), Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(0f to night.copy(alpha = 0.92f), 0.55f to night.copy(alpha = 0.6f), 1f to Color.Transparent)))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.6f to Color.Transparent, 1f to night)))

        Column(
            Modifier.align(Alignment.CenterStart).fillMaxWidth(0.5f).padding(start = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                if (item.type == MediaType.SERIES) "SERIE" else "PELÍCULA",
                color = Color(0xFFD9D2FF), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
            )
            Text(item.title, color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("${item.year} · ${item.genre}", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
            Text(item.synopsis, color = Color.White.copy(alpha = 0.88f), fontSize = 13.sp, lineHeight = 19.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GlassButton(
                    "Reproducir", onPlay,
                    Modifier.focusRequester(playFocus),
                    container = Color.White, contentColor = Color(0xFF1E1740), focusBorder = Color(0xFFC9BEFF),
                    height = 36.dp,
                    leading = { PlayIcon(Color(0xFF1E1740), 12.dp) },
                )
                GlassButton(
                    if (inList) "Quitar de mi lista" else "+ Mi lista", onToggleList,
                    container = Color.White.copy(alpha = 0.22f), focusBorder = Color.White, height = 36.dp,
                )
                GlassButton(
                    "Volver", onBack,
                    container = Color.White.copy(alpha = 0.22f), focusBorder = Color.White, height = 36.dp,
                )
            }
        }
    }
}
