package com.aether.tv.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MediaType
import kotlin.math.abs
import kotlin.math.min

/**
 * Carrusel 3D: la tarjeta del medio al frente y las laterales giradas hacia atrás.
 * Con el control: izquierda/derecha en los extremos de los botones cambia de título.
 */
@Composable
fun HeroCarousel(
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
    val pos = remember { Animatable(index.toFloat()) }
    LaunchedEffect(index) { pos.animateTo(index.toFloat(), tween(420)) }

    BoxWithConstraints(modifier.clipToBounds()) {
        val cardW = min(maxWidth.value * 0.66f, 540f).dp
        val cardH = maxHeight
        val shape = RoundedCornerShape(24.dp)

        items.forEachIndexed { i, m ->
            Box(
                Modifier
                    .align(Alignment.Center)
                    .size(cardW, cardH)
                    .zIndex(-abs(i - index).toFloat())
                    .graphicsLayer {
                        val d = i - pos.value
                        val a = abs(d)
                        translationX = d * size.width * 0.62f
                        val s = 1f - 0.16f * min(a, 2f)
                        scaleX = s
                        scaleY = s
                        rotationY = (d * 38f).coerceIn(-55f, 55f)
                        cameraDistance = 14f * density
                        alpha = (1f - 0.35f * a).coerceIn(0f, 1f)
                    }
                    .clip(shape)
                    .border(1.dp, Color.White.copy(alpha = 0.7f), shape),
            ) {
                SceneArt(Color(m.colorA), Color(m.colorB), Modifier.fillMaxSize())
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.horizontalGradient(0f to Color(0xFF0B0A22).copy(alpha = 0.78f), 0.7f to Color.Transparent),
                    ),
                )
            }
        }

        // Texto y botones: una sola capa fija sobre la tarjeta central, así el foco no se pierde al cambiar de título.
        Box(
            Modifier
                .align(Alignment.Center)
                .size(cardW, cardH)
                .padding(horizontal = 22.dp, vertical = 16.dp),
        ) {
            Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(0.68f)) {
                Box(Modifier.height(112.dp)) {
                    Crossfade(targetState = index, label = "heroText") { i ->
                        val m = items[i.coerceIn(0, items.lastIndex)]
                        Column {
                            Text(
                                if (m.type == MediaType.SERIES) "SERIE" else "PELÍCULA",
                                color = Color(0xFFD9D2FF), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.6.sp,
                            )
                            Text(m.title, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, maxLines = 1)
                            Text("${m.year} · ${m.genre}", color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp, maxLines = 1)
                            Spacer(Modifier.height(4.dp))
                            Text(m.synopsis, color = Color.White.copy(alpha = 0.85f), fontSize = 10.sp, maxLines = 3)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                val cur = items[index.coerceIn(0, items.lastIndex)]
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GlassButton(
                        "Reproducir",
                        onClick = { onPlay(cur) },
                        modifier = Modifier
                            .focusRequester(playFocus)
                            .onPreviewKeyEvent {
                                if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionLeft && index > 0) {
                                    onIndex(index - 1); true
                                } else false
                            },
                        container = Color.White,
                        contentColor = Color(0xFF1E1740),
                        focusBorder = Color(0xFFC9BEFF),
                        leading = { PlayIcon(Color(0xFF1E1740), 10.dp) },
                    )
                    GlassButton(
                        "Más info",
                        onClick = { onInfo(cur) },
                        container = Color.White.copy(alpha = 0.22f),
                        focusBorder = Color.White,
                    )
                    GlassButton(
                        if (inList(cur)) "En mi lista" else "+ Mi lista",
                        onClick = { onToggleList(cur) },
                        modifier = Modifier.onPreviewKeyEvent {
                            if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionRight && index < items.lastIndex) {
                                onIndex(index + 1); true
                            } else false
                        },
                        container = Color.White.copy(alpha = 0.22f),
                        focusBorder = Color.White,
                    )
                }
            }
        }
    }
}

/** Puntos indicadores del carrusel. */
@Composable
fun HeroDots(count: Int, index: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until count) {
            Box(
                Modifier
                    .height(5.dp)
                    .size(width = if (i == index) 22.dp else 6.dp, height = 5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (i == index) AetherColors.Accent else AetherColors.InkSoft.copy(alpha = 0.35f)),
            )
        }
    }
}
