package com.aether.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.aether.tv.data.ContentRepository
import com.aether.tv.data.HomeContent
import com.aether.tv.data.MyList
import com.aether.tv.data.SampleContentRepository
import com.aether.tv.ui.AetherTheme
import com.aether.tv.ui.DetailScreen
import com.aether.tv.ui.PlayerScreen
import com.aether.tv.ui.Shell
import com.aether.tv.ui.SplashScreen
import com.aether.tv.ui.Tab

class MainActivity : ComponentActivity() {
    // Cambiar por la implementación remota cuando exista el backend.
    private val repository: ContentRepository = SampleContentRepository()
    private val myList by lazy { MyList(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AetherTheme { App(repository, myList) } }
    }
}

@Composable
private fun App(repository: ContentRepository, myList: MyList) {
    var splash by rememberSaveable { mutableStateOf(true) }
    var tab by rememberSaveable { mutableStateOf(Tab.INICIO) }
    var detailId by rememberSaveable { mutableStateOf<String?>(null) }
    var playId by rememberSaveable { mutableStateOf<String?>(null) }
    var home by remember { mutableStateOf<HomeContent?>(null) }
    LaunchedEffect(Unit) { home = repository.home() }

    if (splash) {
        SplashScreen { splash = false }
        return
    }

    val content = home
    val byId = remember(content) { content?.library.orEmpty().associateBy { it.id } }
    val playing = playId?.let { byId[it] }
    val detail = detailId?.let { byId[it] }

    when {
        playing != null -> PlayerScreen(playing) { playId = null }
        detail != null -> DetailScreen(
            item = detail,
            inList = myList.contains(detail.id),
            onPlay = { playId = detail.id },
            onToggleList = { myList.toggle(detail.id) },
            onBack = { detailId = null },
        )
        else -> Shell(
            tab = tab,
            onTab = { tab = it },
            content = content,
            myList = myList,
            onOpen = { detailId = it.id },
            onPlay = { playId = it.id },
        )
    }
}
