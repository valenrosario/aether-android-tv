package com.aether.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.aether.tv.data.EmptyContentRepository
import com.aether.tv.ui.AetherTheme
import com.aether.tv.ui.HomeScreen
import com.aether.tv.ui.SettingsScreen
import com.aether.tv.ui.SplashScreen

private enum class Screen { SPLASH, HOME, SETTINGS }

class MainActivity : ComponentActivity() {
    private val repository = EmptyContentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AetherTheme {
                var screen by rememberSaveable { mutableStateOf(Screen.SPLASH) }
                when (screen) {
                    Screen.SPLASH -> SplashScreen { screen = Screen.HOME }
                    Screen.HOME -> HomeScreen(repository) { screen = Screen.SETTINGS }
                    Screen.SETTINGS -> SettingsScreen { screen = Screen.HOME }
                }
            }
        }
    }
}
