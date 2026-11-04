package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
        Column(Modifier.fillMaxSize()) {
            TopBar(tab, onTab)
            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (content != null) {
                    val pad = Modifier.fillMaxSize().padding(horizontal = 32.dp)
                    when (tab) {
                        Tab.INICIO -> HomeScreen(content, myList, onOpen, onPlay)
                        Tab.BUSCAR -> Box(pad) { SearchScreen(content.library, onOpen) }
                        Tab.PELICULAS -> Box(pad) { LibraryScreen("Películas", content.library.filter { it.type == MediaType.MOVIE }, onOpen) }
                        Tab.SERIES -> Box(pad) { LibraryScreen("Series", content.library.filter { it.type == MediaType.SERIES }, onOpen) }
                        Tab.LISTA -> Box(pad) {
                            LibraryScreen(
                                "Mi lista",
                                content.library.filter { it.id in myList.ids },
                                onOpen,
                                emptyMessage = "Todavía no agregaste nada. Usá “+ Mi lista” en cualquier título.",
                            )
                        }
                        Tab.AJUSTES -> Box(pad) { SettingsContent() }
                    }
                }
            }
        }
    }
}

/** Barra superior: logo a la izquierda y secciones como texto con subrayado en la activa. */
@Composable
private fun TopBar(tab: Tab, onTab: (Tab) -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(46.dp).padding(horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AetherWordmark(markSize = 20.dp, textSize = 15)
        Spacer(Modifier.width(18.dp))
        Tab.values().forEach { t ->
            val sel = t == tab
            GlassFocus(
                Modifier.height(30.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { onTab(t) },
            ) {
                Column(Modifier.align(Alignment.Center).padding(horizontal = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        t.label,
                        color = if (sel) Color.White else AetherColors.InkSoft,
                        fontSize = 11.sp,
                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                    )
                    Box(
                        Modifier.padding(top = 2.dp).size(width = 16.dp, height = 2.dp)
                            .background(if (sel) AetherColors.Accent else Color.Transparent),
                    )
                }
            }
        }
    }
}
