package com.aether.tv.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.aether.tv.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(val version: String, val notes: String, val apkUrl: String, val sizeBytes: Long)

/** Busca versiones nuevas en GitHub Releases de `aether-android-tv`. */
object UpdateChecker {

    private val api get() =
        "https://api.github.com/repos/${BuildConfig.GITHUB_OWNER}/${BuildConfig.GITHUB_REPO}/releases/latest"

    /** Devuelve la actualización si hay una versión más nueva con APK adjunto; null si no. */
    suspend fun check(): UpdateInfo? = withContext(Dispatchers.IO) {
        val conn = (URL(api).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Accept", "application/vnd.github+json")
        }
        try {
            if (conn.responseCode != 200) return@withContext null
            val json = JSONObject(conn.inputStream.bufferedReader().use { it.readText() })
            val tag = json.getString("tag_name").removePrefix("v")
            if (!isNewer(tag, BuildConfig.VERSION_NAME)) return@withContext null
            val assets = json.getJSONArray("assets")
            for (i in 0 until assets.length()) {
                val a = assets.getJSONObject(i)
                if (a.getString("name").endsWith(".apk")) {
                    return@withContext UpdateInfo(
                        version = tag,
                        notes = json.optString("body"),
                        apkUrl = a.getString("browser_download_url"),
                        sizeBytes = a.optLong("size"),
                    )
                }
            }
            null
        } finally {
            conn.disconnect()
        }
    }

    internal fun isNewer(remote: String, local: String): Boolean {
        fun parts(v: String) = v.substringBefore('-').split('.').map { it.toIntOrNull() ?: 0 }
        val r = parts(remote)
        val l = parts(local)
        for (i in 0 until maxOf(r.size, l.size)) {
            val a = r.getOrElse(i) { 0 }
            val b = l.getOrElse(i) { 0 }
            if (a != b) return a > b
        }
        return false
    }

    suspend fun download(context: Context, info: UpdateInfo, onProgress: (Float) -> Unit): File =
        withContext(Dispatchers.IO) {
            val dir = File(context.cacheDir, "updates").apply { mkdirs(); listFiles()?.forEach { it.delete() } }
            val out = File(dir, "Aether-${info.version}.apk")
            val conn = URL(info.apkUrl).openConnection() as HttpURLConnection
            conn.connectTimeout = 15_000
            conn.readTimeout = 30_000
            try {
                val total = conn.contentLengthLong.takeIf { it > 0 } ?: info.sizeBytes
                conn.inputStream.use { input ->
                    out.outputStream().use { output ->
                        val buf = ByteArray(64 * 1024)
                        var done = 0L
                        while (true) {
                            val n = input.read(buf)
                            if (n < 0) break
                            output.write(buf, 0, n)
                            done += n
                            if (total > 0) onProgress(done.toFloat() / total)
                        }
                    }
                }
            } finally {
                conn.disconnect()
            }
            out
        }

    /** Lanza el instalador del sistema. Devuelve false si falta el permiso de orígenes desconocidos. */
    fun install(context: Context, apk: File): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.packageManager.canRequestPackageInstalls()) {
            context.startActivity(
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            return false
        }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apk)
        context.startActivity(
            Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, "application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        return true
    }
}
