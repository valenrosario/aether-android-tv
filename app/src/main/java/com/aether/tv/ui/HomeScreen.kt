package com.aether.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.ContentRepository
import com.aether.tv.data.Rail

@Composable
fun HomeScreen(repository: ContentRepository, onOpenSettings: () -> Unit) {
    var rails by remember { mutableStateOf<List<Rail>?>(null) }
    LaunchedEffect(Unit) { rails = repository.home() }

    Column(Modifier.fillMaxSize().background(AetherColors.Black).padding(horizontal = 56.dp, vertical = 32.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AetherWordmark()
            Spacer(Modifier.weight(1f))
            AetherButton("Ajustes", onClick = onOpenSettings)
        }
        Spacer(Modifier.height(32.dp))

        val data = rails
        when {
            data == null -> Unit
            data.isEmpty() -> EmptyState()
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(28.dp)) {
                items(data) { rail ->
                    Text(rail.title, color = AetherColors.Halo, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(rail.items) { item ->
                            FocusBox(Modifier.size(width = 168.dp, height = 252.dp)) {
                                Text(item.title, color = AetherColors.Halo, fontSize = 16.sp, modifier = Modifier.padding(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Tu catálogo aparecerá acá", color = AetherColors.Halo, fontSize = 28.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(8.dp))
        Text("Todavía no hay una fuente de contenido conectada.", color = AetherColors.Muted, fontSize = 18.sp)
    }
}
