package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MediaType

private val EP_TITLES = listOf(
    "Bienvenidos", "Lo que quedó atrás", "Una señal en la noche", "El precio de la verdad",
    "Frente al mar", "Cartas sin remitente", "La hora azul", "Todo lo que callamos",
)

/**
 * Ficha del título: arte de fondo, título grande, géneros y botones a la izquierda;
 * en series, panel de temporadas y capítulos a la derecha.
 */
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
    val isSeries = item.type == MediaType.SERIES

    Box(Modifier.fillMaxSize().background(AetherColors.Black)) {
        SceneArt(Color(item.colorA), Color(item.colorB), Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(0f to Color.Black.copy(alpha = 0.9f), 0.6f to Color.Black.copy(alpha = 0.45f), 1f to Color.Transparent)))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.6f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.85f))))

        // Botón volver (círculo) arriba a la izquierda.
        GlassFocus(
            Modifier.padding(start = 24.dp, top = 20.dp).size(34.dp),
            shape = RoundedCornerShape(17.dp),
            fillColor = Color.White.copy(alpha = 0.16f),
            onClick = onBack,
        ) { Text("‹", color = Color.White, fontSize = 22.sp, modifier = Modifier.align(Alignment.Center)) }

        Column(
            Modifier.align(Alignment.CenterStart).padding(start = 48.dp).fillMaxWidth(if (isSeries) 0.46f else 0.6f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                item.title.uppercase(), color = Color.White, fontSize = 36.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 4.sp, maxLines = 2,
            )
            Text(
                "${item.year ?: ""}   ·   " + if (isSeries) "Serie" else "Película",
                color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp,
            )
            Text("GÉNEROS", color = AetherColors.InkSoft, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.genre.split("·").map { it.trim() }.filter { it.isNotEmpty() }.forEach { g ->
                    Box(
                        Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.14f))
                            .padding(horizontal = 12.dp, vertical = 5.dp),
                    ) { Text(g, color = Color.White, fontSize = 10.sp) }
                }
            }
            Text(item.synopsis, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, lineHeight = 17.sp, maxLines = 4)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GlassButton(
                    "Reproducir", onPlay, Modifier.focusRequester(playFocus),
                    container = Color.White, contentColor = Color(0xFF111111), height = 36.dp,
                    leading = { PlayIcon(Color(0xFF111111), 12.dp) },
                )
                GlassButton(
                    if (inList) "En mi lista" else "+ Mi lista", onToggleList,
                    container = Color.White.copy(alpha = 0.18f), height = 36.dp,
                )
            }
        }

        if (isSeries) EpisodePanel(onPlay, Modifier.align(Alignment.CenterEnd).padding(end = 24.dp, top = 20.dp, bottom = 20.dp).width(300.dp).fillMaxHeight())
    }
}

@Composable
private fun EpisodePanel(onPlay: () -> Unit, modifier: Modifier) {
    var season by remember { mutableStateOf(1) }
    Column(
        modifier.glass(RoundedCornerShape(20.dp), fill = 0.9f).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            GlassButton("‹ Ant.", { if (season > 1) season-- }, container = Color.White.copy(alpha = 0.12f), height = 26.dp)
            Text("Temporada $season", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            GlassButton("Sig. ›", { if (season < 3) season++ }, container = Color.White.copy(alpha = 0.12f), height = 26.dp)
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxSize()) {
            items(EP_TITLES.size) { i ->
                val a = Color(0xFF3B2F8F + (season * 0x080C10L)); val b = Color(0xFF0F1230)
                GlassFocus(Modifier.fillMaxWidth().height(58.dp), RoundedCornerShape(10.dp), onClick = onPlay) {
                    Row(Modifier.fillMaxSize().padding(6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(width = 76.dp, height = 44.dp).clip(RoundedCornerShape(6.dp)).posterArt(a, b))
                        Column {
                            Text("${i + 1}. ${EP_TITLES[(i + season) % EP_TITLES.size]}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
                            Text("T$season · E${i + 1}", color = AetherColors.InkSoft, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}
