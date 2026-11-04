package com.aether.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.MediaItem

@Composable
fun LibraryScreen(
    title: String,
    items: List<MediaItem>,
    onOpen: (MediaItem) -> Unit,
    emptyMessage: String = "No hay títulos en esta sección.",
) {
    Column(Modifier.fillMaxSize()) {
        Text(title, color = AetherColors.Ink, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
        Spacer(Modifier.height(6.dp))
        if (items.isEmpty()) EmptyState(emptyMessage) else PosterGrid(items, onOpen)
    }
}

@Composable
private fun PosterGrid(items: List<MediaItem>, onOpen: (MediaItem) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(136.dp),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items) { m -> PosterCard(m) { onOpen(m) } }
    }
}

@Composable
fun SearchScreen(library: List<MediaItem>, onOpen: (MediaItem) -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = remember(query, library) {
        val q = query.trim()
        if (q.isEmpty()) library else library.filter {
            it.title.contains(q, ignoreCase = true) || it.genre.contains(q, ignoreCase = true)
        }
    }
    Column(Modifier.fillMaxSize()) {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            textStyle = TextStyle(color = AetherColors.Ink, fontSize = 16.sp),
            cursorBrush = SolidColor(AetherColors.Accent),
            modifier = Modifier.fillMaxWidth().glass(RoundedCornerShape(24.dp)).padding(horizontal = 20.dp, vertical = 12.dp),
            decorationBox = { inner ->
                Box {
                    if (query.isEmpty()) Text("Buscar títulos o géneros", color = AetherColors.InkSoft, fontSize = 16.sp)
                    inner()
                }
            },
        )
        Spacer(Modifier.height(4.dp))
        if (results.isEmpty()) EmptyState("No encontramos resultados para “$query”.") else PosterGrid(results, onOpen)
    }
}
