package com.aether.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.aether.tv.BuildConfig
import com.aether.tv.update.UpdateChecker
import com.aether.tv.update.UpdateInfo
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }
    var update by remember { mutableStateOf<UpdateInfo?>(null) }
    var busy by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        AuroraBackground()
        Column(
            Modifier.fillMaxSize().padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AetherWordmark()
            Spacer(Modifier.height(20.dp))
            Text("Ajustes", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Light)
            Text("Aether ${BuildConfig.VERSION_NAME}", color = Color.White.copy(alpha = 0.75f), fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))

            AetherButton(if (update == null) "Buscar actualizaciones" else "Instalar Aether ${update!!.version}") {
                if (busy) return@AetherButton
                scope.launch {
                    busy = true
                    try {
                        val found = update
                        if (found == null) {
                            status = "Buscando…"
                            update = UpdateChecker.check()
                            status = if (update == null) "Tenés la última versión." else "Hay una versión nueva: ${update!!.version}"
                        } else {
                            val apk = UpdateChecker.download(context, found) { p -> status = "Descargando ${(p * 100).toInt()}%" }
                            status = if (UpdateChecker.install(context, apk)) "Abriendo instalador…"
                            else "Habilitá la instalación desde Aether y volvé a intentar."
                        }
                    } catch (e: Exception) {
                        status = "No se pudo completar. Revisá tu conexión."
                    } finally {
                        busy = false
                    }
                }
            }
            if (status.isNotEmpty()) Text(status, color = Color.White.copy(alpha = 0.75f), fontSize = 14.sp)
        }
    }
}
