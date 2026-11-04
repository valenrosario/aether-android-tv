package com.aether.tv.data

/** Catálogo de ejemplo (títulos ficticios) hasta conectar el contenido real. */
class SampleContentRepository : ContentRepository {

    private fun s(
        id: String, title: String, genre: String, a: Long, b: Long,
        progress: Float? = null, synopsis: String = "", year: Int = 2026,
    ) = MediaItem(id, title, MediaType.SERIES, synopsis, genre, year, colorA = a, colorB = b, progress = progress)

    override suspend fun home(): HomeContent {
        val featured = s(
            "noche-cobalto", "Noche Cobalto", "Thriller · Misterio", 0xFF4A2AA8, 0xFF1B1F6B,
            synopsis = "Un faro apagado, una ciudad sin luz y una investigadora que descubre que el apagón no fue un accidente.",
        )
        val top = listOf(
            featured,
            s("marea-alta", "Marea Alta", "Drama", 0xFF2F7FE0, 0xFF0A1A3A),
            s("ceniza-dorada", "Ceniza Dorada", "Histórica", 0xFFE0A63A, 0xFF3A210A),
            s("sur-profundo", "Sur Profundo", "Thriller", 0xFF2FC2A2, 0xFF0A2A28),
        )
        val continuar = Rail(
            "Continuar viendo",
            listOf(
                s("c1", "Noche Cobalto", "T1 · E4", 0xFF6A5CFF, 0xFF1A1450, 0.62f),
                s("c2", "La Última Frecuencia", "T2 · E1", 0xFF0E8FA6, 0xFF0A1A2A, 0.30f),
                s("c3", "Costa Brava 1987", "T1 · E7", 0xFFFF6A4A, 0xFF3A0F1A, 0.85f),
                s("c4", "Hermanas del Faro", "T1 · E2", 0xFF2FA06F, 0xFF0B1F1A, 0.12f),
                s("c5", "Polvo de Estrellas", "T3 · E5", 0xFFA85CFF, 0xFF190A3A, 0.45f),
                s("c6", "Mar de Cenizas", "T1 · E3", 0xFF7A8AA8, 0xFF0D1220, 0.70f),
            ),
        )
        val paraTi = Rail(
            "Para ti",
            listOf(
                s("p1", "Los Cartógrafos", "Aventura", 0xFFE08A3A, 0xFF2A1306),
                s("p2", "El Jardín de Hielo", "Fantasía", 0xFF5FB4FF, 0xFF0D1F4A),
                s("p3", "Vértigo Azul", "Suspenso", 0xFF7A5CFF, 0xFF150F4A),
                s("p4", "Ruta 40", "Road movie", 0xFFFFB347, 0xFF3A1A0A),
                s("p5", "Casa Umbría", "Terror", 0xFFC04A8A, 0xFF1A0716),
                s("p6", "Eco de Sal", "Documental", 0xFF3AB8C8, 0xFF082026),
                s("p7", "Marea Alta", "Drama", 0xFF2F7FE0, 0xFF0A1A3A),
                s("p8", "Ceniza Dorada", "Histórica", 0xFFE0A63A, 0xFF3A210A),
            ),
        )
        return HomeContent(featured, top, listOf(continuar, paraTi))
    }
}
