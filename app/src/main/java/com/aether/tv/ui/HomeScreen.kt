package com.aether.tv.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.ContentRepository
import com.aether.tv.data.HomeContent
import com.aether.tv.data.MediaItem

@Composable
fun HomeScreen(repository: ContentRepository, onOpenSettings: () -> Unit) {
    var content by remember { mutableStateOf<HomeContent?>(null) }
    LaunchedEffect(Unit) { content = repository.home() }

    Box(Modifier.fillMaxSize()) {
        AuroraBackground()
        val c = content
        val featured = c?.featured
        if (c != null && featured != null) Catalog(c, featured) else if (c != null) EmptyState()
        TopBar(onOpenSettings)
    }
}

@Composable
private fun Catalog(content: HomeContent, featured: MediaItem) {
    val playFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { playFocus.requestFocus() } }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 40.dp, end = 40.dp, top = 92.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth().height(250.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Hero(featured, playFocus, Modifier.weight(1f).fillMaxHeight())
                TopPanel(content.top, Modifier.width(300.dp).fillMaxHeight())
            }
        }
        items(content.rails) { rail ->
            Column {
                Text(rail.title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp),
                ) {
                    items(rail.items) { PosterCard(it) }
                }
            }
        }
    }
}

@Composable
private fun Hero(item: MediaItem, playFocus: FocusRequester, modifier: Modifier) {
    val shape = RoundedCornerShape(24.dp)
    Box(modifier.clip(shape).border(1.dp, Color.White.copy(alpha = 0.35f), shape)) {
        SceneArt(Color(item.colorA), Color(item.colorB), Modifier.fillMaxSize())
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .width(330.dp)
                .glass(RoundedCornerShape(18.dp), 0.22f)
                .padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Text("SERIE ORIGINAL · NUEVA TEMPORADA", color = Color(0xFFE3DDFF), fontSize = 8.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp)
            Text(item.title, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("${item.year} · ${item.genre} · +16", color = Color.White.copy(alpha = 0.88f), fontSize = 10.sp)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                GlassFocus(
                    Modifier.height(32.dp).focusRequester(playFocus),
                    shape = RoundedCornerShape(16.dp),
                    fillColor = Color.White,
                    focusBorder = Color(0xFFC9BEFF),
                ) {
                    Row(
                        Modifier.align(Alignment.Center).padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        PlayIcon(Color(0xFF0B0A22), 11.dp)
                        Text("Reproducir", color = Color(0xFF0B0A22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                GlassFocus(Modifier.size(32.dp), CircleShape, Color.White.copy(alpha = 0.16f)) {
                    Text("+", Modifier.align(Alignment.Center), color = Color.White, fontSize = 16.sp)
                }
                GlassFocus(Modifier.size(32.dp), CircleShape, Color.White.copy(alpha = 0.16f)) {
                    Text("i", Modifier.align(Alignment.Center), color = Color.White, fontSize = 14.sp, fontFamily = FontFamily.Serif)
                }
            }
        }
    }
}

@Composable
private fun TopPanel(top: List<MediaItem>, modifier: Modifier) {
    Column(modifier.glass(RoundedCornerShape(24.dp), 0.18f).padding(horizontal = 12.dp, vertical = 12.dp)) {
        Text("Top 10 hoy", Modifier.padding(start = 6.dp, bottom = 6.dp), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        top.take(4).forEachIndexed { i, m ->
            GlassFocus(
                Modifier.fillMaxWidth().padding(vertical = 2.dp),
                shape = RoundedCornerShape(14.dp),
                fillColor = if (i == 0) Color.White.copy(alpha = 0.18f) else Color.Transparent,
            ) {
                Row(Modifier.padding(horizontal = 9.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${i + 1}", Modifier.width(24.dp), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    Box(Modifier.size(width = 28.dp, height = 38.dp).clip(RoundedCornerShape(7.dp)).posterArt(Color(m.colorA), Color(m.colorB)))
                    Column(Modifier.padding(start = 10.dp)) {
                        Text(m.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, maxLines = 1)
                        Text(m.genre, color = Color.White.copy(alpha = 0.78f), fontSize = 9.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun PosterCard(item: MediaItem) {
    val shape = RoundedCornerShape(16.dp)
    GlassFocus(Modifier.size(width = 107.dp, height = 148.dp), shape) {
        Box(Modifier.fillMaxSize().posterArt(Color(item.colorA), Color(item.colorB)))
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(5.dp)
                .glass(RoundedCornerShape(12.dp), 0.26f)
                .padding(horizontal = 8.dp, vertical = 7.dp),
        ) {
            Text(item.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, maxLines = 2)
            Text(item.genre, color = Color.White.copy(alpha = 0.85f), fontSize = 8.sp, maxLines = 1)
            val p = item.progress
            if (p != null) {
                Spacer(Modifier.height(4.dp))
                Box(Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.25f))) {
                    Box(Modifier.fillMaxWidth(p).fillMaxHeight().background(Color.White))
                }
            }
        }
    }
}

@Composable
private fun TopBar(onOpenSettings: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(top = 18.dp, start = 40.dp, end = 40.dp)) {
        Row(
            Modifier
                .align(Alignment.TopCenter)
                .width(500.dp)
                .height(44.dp)
                .glass(RoundedCornerShape(22.dp), 0.2f)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AetherWordmark(markSize = 22.dp, textSize = 12)
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("Inicio", "Series", "Películas", "Mi lista").forEachIndexed { i, t ->
                    GlassFocus(
                        shape = RoundedCornerShape(15.dp),
                        fillColor = if (i == 0) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                    ) {
                        Text(t, Modifier.padding(horizontal = 11.dp, vertical = 6.dp), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            GlassFocus(Modifier.size(28.dp), CircleShape, Color.White.copy(alpha = 0.14f)) {
                Box(Modifier.align(Alignment.Center)) { SearchIcon(Color.White, 14.dp) }
            }
        }
        GlassFocus(
            Modifier.align(Alignment.TopEnd).size(44.dp),
            CircleShape,
            Color.White.copy(alpha = 0.16f),
            onClick = onOpenSettings,
        ) {
            Box(Modifier.align(Alignment.Center)) { GearIcon(Color.White, 20.dp) }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Tu catálogo aparecerá acá", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(6.dp))
        Text("Todavía no hay una fuente de contenido conectada.", color = AetherColors.Muted, fontSize = 14.sp)
    }
}
