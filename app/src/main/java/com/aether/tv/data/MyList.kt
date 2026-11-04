package com.aether.tv.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** "Mi lista" del usuario, guardada en el dispositivo. */
class MyList(context: Context) {
    private val prefs = context.getSharedPreferences("aether", Context.MODE_PRIVATE)

    var ids: Set<String> by mutableStateOf(prefs.getStringSet("mylist", emptySet())?.toSet() ?: emptySet())
        private set

    fun contains(id: String) = id in ids

    fun toggle(id: String) {
        ids = if (id in ids) ids - id else ids + id
        prefs.edit().putStringSet("mylist", ids.toSet()).apply()
    }
}
