package com.aether.tv.data

enum class MediaType { MOVIE, SERIES }

data class MediaItem(
    val id: String,
    val title: String,
    val type: MediaType,
    val synopsis: String = "",
    val genre: String = "",
    val year: Int? = null,
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    /** Colores de portada mientras no haya imagen (ARGB). */
    val colorA: Long = 0xFF3B2F8F,
    val colorB: Long = 0xFF0F1230,
    /** 0..1 si el usuario ya empezó a verlo. */
    val progress: Float? = null,
)

data class Rail(val title: String, val items: List<MediaItem>)

data class HomeContent(
    val featured: MediaItem?,
    val top: List<MediaItem>,
    val rails: List<Rail>,
)

/**
 * Contrato de contenido. La UI depende solo de esta interfaz: cuando exista el backend
 * se agrega una implementación remota sin tocar las pantallas.
 */
interface ContentRepository {
    suspend fun home(): HomeContent
}

class EmptyContentRepository : ContentRepository {
    override suspend fun home() = HomeContent(null, emptyList(), emptyList())
}
