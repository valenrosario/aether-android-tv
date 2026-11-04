package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.data.HomeContent
import com.aether.tv.data.MediaItem
import com.aether.tv.data.MediaType
import com.aether.tv.data.MyList

enum class Tab(val label: String, val icon: NavKind) {
    INICIO("Inicio", NavKind.HOME),
    BUSCAR("Buscar", NavKind.SEARCH),
    PELICULAS("Películas", NavKind.FILM),
    SERIES("Series", NavKind.TV),
    LISTA("Mi lista", NavKind.LIST),
    AJUSTES("Ajustes", NavKind.GEAR),
}

/** Estructura fija: barra lateral a la izquierda y el contenido de la sección a la derecha. */
@Composable
fun Shell(
    tab: Tab,
    onTab: (Tab) -> Unit,
    content: HomeContent?,
    myList: MyList,
    onOpen: (MediaItem) -> Unit,
    onPlay: (MediaItem) -> Unit,
) {
    BackHandler(enabled = tab != Tab.INICIO) { onTab(Tab.INICIO) }

    Box(Modifier.fillMaxSize()) {
        AuroraBackground()
        Row(
            Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Rail(tab, onTab, Modifier.width(72.dp).fillMaxHeight())
            Box(Modifier.weight(1f).fillMaxHeight()) {
                if (content != null) {
                    when (tab) {
                        Tab.INICIO -> HomeScreen(content, myList, onOpen, onPlay)
                        Tab.BUSCAR -> SearchScreen(content.library, onOpen)
                        Tab.PELICULAS -> LibraryScreen("Películas", content.library.filter { it.type == MediaType.MOVIE }, onOpen)
                        Tab.SERIES -> LibraryScreen("Series", content.library.filter { it.type == MediaType.SERIES }, onOpen)
                        Tab.LISTA -> LibraryScreen(
                            "Mi lista",
                            content.library.filter { it.id in myList.ids },
                            onOpen,
                            emptyMessage = "Todavía no agregaste nada. Usá “+ Mi lista” en cualquier título.",
                        )
                        Tab.AJUSTES -> SettingsContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun Rail(tab: Tab, onTab: (Tab) -> Unit, modifier: Modifier) {
    Column(
        modifier.glass(RoundedCornerShape(36.dp)).padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        AetherMark(28.dp, color = AetherColors.Ink)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Tab.values().filter { it != Tab.AJUSTES }.forEach { RailItem(it, it == tab, onTab) }
        }
        RailItem(Tab.AJUSTES, tab == Tab.AJUSTES, onTab)
    }
}

@Composable
private fun RailItem(t: Tab, selected: Boolean, onTab: (Tab) -> Unit) {
    val fg = if (selected) Color.White else AetherColors.InkSoft
    GlassFocus(
        Modifier.width(56.dp).height(50.dp),
        shape = RoundedCornerShape(18.dp),
        fillColor = if (selected) AetherColors.Accent else Color.Transparent,
        focusBorder = AetherColors.Ink,
        onClick = { onTab(t) },
    ) {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            NavIcon(t.icon, fg, 20.dp)
            Text(t.label, color = fg, fontSize = 8.sp, maxLines = 1)
        }
    }
}
