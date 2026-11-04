package com.aether.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem

private val scrim = Brush.verticalGradient(0.45f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.75f))

@Composable
fun PosterCard(item: MediaItem, onClick: () -> Unit) {
    GlassFocus(Modifier.size(width = 128.dp, height = 192.dp), RoundedCornerShape(8.dp), onClick = onClick) {
        Box(Modifier.fillMaxSize().posterArt(Color(item.colorA), Color(item.colorB)))
        Box(Modifier.fillMaxSize().background(scrim))
        Column(Modifier.align(Alignment.BottomStart).padding(8.dp)) {
            Text(item.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, maxLines = 2)
            Text(item.genre, color = Color.White.copy(alpha = 0.85f), fontSize = 9.sp, maxLines = 1)
        }
    }
}

/** Tarjeta horizontal con barra de progreso (continuar viendo). */
@Composable
fun LandscapeCard(item: MediaItem, onClick: () -> Unit) {
    GlassFocus(Modifier.size(width = 208.dp, height = 117.dp), RoundedCornerShape(8.dp), onClick = onClick) {
        Box(Modifier.fillMaxSize().posterArt(Color(item.colorA), Color(item.colorB)))
        Box(Modifier.fillMaxSize().background(scrim))
        Column(Modifier.align(Alignment.BottomStart).padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
            Text(item.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, maxLines = 1)
            Text(item.genre, color = Color.White.copy(alpha = 0.85f), fontSize = 9.sp, maxLines = 1)
        }
        val p = item.progress
        if (p != null) {
            Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(3.dp).background(Color.White.copy(alpha = 0.3f))) {
                Box(Modifier.fillMaxWidth(p).fillMaxHeight().background(Color(0xFFC9BEFF)))
            }
        }
    }
}
