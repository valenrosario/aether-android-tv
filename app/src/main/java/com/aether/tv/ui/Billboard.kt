package com.aether.tv.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MediaType
import kotlinx.coroutines.delay

/**
 * Billboard a todo el ancho: arte del título de fondo, título grande, datos, sinopsis y botones.
 * Rota solo cada 8 s; con el control, izquierda en "Reproducir" y derecha en "+ Mi lista" cambian de título.
 */
@Composable
fun Billboard(
    items: List<MediaItem>,
    index: Int,
    onIndex: (Int) -> Unit,
    inList: (MediaItem) -> Boolean,
    onPlay: (MediaItem) -> Unit,
    onInfo: (MediaItem) -> Unit,
    onToggleList: (MediaItem) -> Unit,
    playFocus: FocusRequester,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return
    val safe = index.coerceIn(0, items.lastIndex)
    LaunchedEffect(safe, items.size) {
        delay(8000)
        onIndex((safe + 1) % items.size)
    }
    val cur = items[safe]

    Box(modifier) {
        Crossfade(targetState = safe, animationSpec = tween(700), label = "billboardArt") { i ->
            val m = items[i.coerceIn(0, items.lastIndex)]
            SceneArt(Color(m.colorA), Color(m.colorB), Modifier.fillMaxSize())
        }
        // Degradés: izquierda para leer el texto, abajo para fundirse con las filas.
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(0f to Color.Black.copy(alpha = 0.85f), 0.6f to Color.Transparent)))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.55f to Color.Transparent, 1f to AetherColors.Black)))

        Column(Modifier.align(Alignment.BottomStart).padding(start = 32.dp, bottom = 22.dp).fillMaxWidth(0.55f)) {
            Text(
                "AETHER  ·  " + if (cur.type == MediaType.SERIES) "SERIE" else "PELÍCULA",
                color = Color(0xFFB9AEFF), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
            )
            Text(
                cur.title.uppercase(), color = Color.White, fontSize = 34.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 4.sp, maxLines = 1,
            )
            Text("${cur.year ?: ""}  ·  ${cur.genre}", color = Color.White.copy(alpha = 0.85f), fontSize = 10.sp, maxLines = 1)
            Spacer(Modifier.height(6.dp))
            Text(cur.synopsis, color = Color.White.copy(alpha = 0.85f), fontSize = 10.sp, maxLines = 3, lineHeight = 14.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassButton(
                    "Reproducir",
                    onClick = { onPlay(cur) },
                    modifier = Modifier
                        .focusRequester(playFocus)
                        .onPreviewKeyEvent {
                            if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionLeft && safe > 0) {
                                onIndex(safe - 1); true
                            } else false
                        },
                    container = Color.White,
                    contentColor = Color(0xFF111111),
                    height = 32.dp,
                    leading = { PlayIcon(Color(0xFF111111), 10.dp) },
                )
                GlassButton("Más info", onClick = { onInfo(cur) }, container = Color.White.copy(alpha = 0.22f), height = 32.dp)
                GlassButton(
                    if (inList(cur)) "En mi lista" else "+ Mi lista",
                    onClick = { onToggleList(cur) },
                    modifier = Modifier.onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionRight && safe < items.lastIndex) {
                            onIndex(safe + 1); true
                        } else false
                    },
                    container = Color.White.copy(alpha = 0.22f),
                    height = 32.dp,
                )
            }
        }

        // Indicadores (barritas) abajo a la derecha.
        Row(
            Modifier.align(Alignment.BottomEnd).padding(end = 32.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (i in items.indices) {
                Box(
                    Modifier
                        .size(width = if (i == safe) 22.dp else 12.dp, height = 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (i == safe) Color.White else Color.White.copy(alpha = 0.3f)),
                )
            }
        }
    }
}
