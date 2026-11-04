package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.aether.tv.data.DEMO_VIDEO
import com.aether.tv.data.MediaItem

/** Reproductor a pantalla completa. Con el control: OK muestra/oculta controles, izquierda/derecha adelanta o retrocede, Atrás vuelve. */
@Composable
fun PlayerScreen(item: MediaItem, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    val exo = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(ExoMediaItem.fromUri(item.videoUrl ?: DEMO_VIDEO))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(Unit) { onDispose { exo.release() } }

    AndroidView(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exo
                useController = true
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
        },
    )
}
