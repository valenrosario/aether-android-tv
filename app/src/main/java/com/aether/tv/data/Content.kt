package com.aether.tv.data

enum class MediaType { MOVIE, SERIES }

data class MediaItem(
    val id: String,
    val title: String,
    val type: MediaType,
    val synopsis: String = "",
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    val year: Int? = null,
)

data class Rail(val title: String, val items: List<MediaItem>)

/**
 * Contrato de contenido. La UI depende solo de esta interfaz: cuando exista el backend
 * se agrega una implementación remota sin tocar las pantallas.
 */
interface ContentRepository {
    suspend fun home(): List<Rail>
}

/** Implementación inicial sin backend: catálogo vacío. */
class EmptyContentRepository : ContentRepository {
    override suspend fun home(): List<Rail> = emptyList()
}
