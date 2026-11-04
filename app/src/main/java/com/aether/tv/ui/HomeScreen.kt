package com.aether.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.HomeContent
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MyList

@Composable
fun HomeScreen(
    content: HomeContent,
    myList: MyList,
    onOpen: (MediaItem) -> Unit,
    onPlay: (MediaItem) -> Unit,
) {
    var heroIndex by rememberSaveable { mutableIntStateOf(0) }
    val playFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { playFocus.requestFocus() } }

    if (content.featured.isEmpty() && content.rails.isEmpty()) {
        EmptyState()
        return
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        item {
            Billboard(
                items = content.featured,
                index = heroIndex,
                onIndex = { heroIndex = it },
                inList = { myList.contains(it.id) },
                onPlay = onPlay,
                onInfo = onOpen,
                onToggleList = { myList.toggle(it.id) },
                playFocus = playFocus,
                modifier = Modifier.fillMaxWidth().height(270.dp),
            )
        }
        items(content.rails) { rail ->
            Column {
                Text(rail.title, color = AetherColors.Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 32.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 10.dp),
                ) {
                    items(rail.items) { m ->
                        if (rail.wide) LandscapeCard(m) { onOpen(m) } else PosterCard(m) { onOpen(m) }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String = "Todavía no hay contenido conectado.") {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(message, color = AetherColors.InkSoft, fontSize = 16.sp)
    }
}
