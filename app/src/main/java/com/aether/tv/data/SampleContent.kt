package com.aether.tv.data

/** Video de prueba (Big Buck Bunny, licencia CC-BY) para que Reproducir funcione hasta tener contenido real. */
const val DEMO_VIDEO = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

/** Catálogo de ejemplo (títulos ficticios) hasta conectar el contenido real. */
class SampleContentRepository : ContentRepository {

    private fun series(id: String, title: String, genre: String, a: Long, b: Long, synopsis: String) =
        MediaItem(id, title, MediaType.SERIES, synopsis, genre, 2026, videoUrl = DEMO_VIDEO, colorA = a, colorB = b)

    private fun movie(id: String, title: String, genre: String, a: Long, b: Long, synopsis: String) =
        MediaItem(id, title, MediaType.MOVIE, synopsis, genre, 2025, videoUrl = DEMO_VIDEO, colorA = a, colorB = b)

    override suspend fun home(): HomeContent {
        val noche = series("noche-cobalto", "Noche Cobalto", "Thriller · Misterio", 0xFF4A2AA8, 0xFF1B1F6B,
            "Un faro apagado, una ciudad sin luz y una investigadora que descubre que el apagón no fue un accidente.")
        val frecuencia = series("ultima-frecuencia", "La Última Frecuencia", "Ciencia ficción", 0xFF0E8FA6, 0xFF0A1A2A,
            "Una radioaficionada capta una señal que anuncia lo que va a pasar mañana.")
        val costa = series("costa-brava", "Costa Brava 1987", "Drama", 0xFFFF6A4A, 0xFF3A0F1A,
            "Un verano, tres familias y un secreto que nadie quiere nombrar.")
        val hermanas = series("hermanas-faro", "Hermanas del Faro", "Misterio", 0xFF2FA06F, 0xFF0B1F1A,
            "Dos hermanas heredan un faro y los misterios que guarda.")
        val polvo = series("polvo-estrellas", "Polvo de Estrellas", "Ciencia ficción", 0xFFA85CFF, 0xFF190A3A,
            "La tripulación de una estación espacial empieza a recordar vidas que no vivió.")
        val cenizas = series("mar-cenizas", "Mar de Cenizas", "Misterio", 0xFF7A8AA8, 0xFF0D1220,
            "Tras la erupción, un pueblo costero esconde quién la provocó.")
        val marea = series("marea-alta", "Marea Alta", "Drama", 0xFF2F7FE0, 0xFF0A1A3A,
            "Una familia de pescadores enfrenta la tormenta que cambia el puerto.")
        val sur = series("sur-profundo", "Sur Profundo", "Thriller", 0xFF2FC2A2, 0xFF0A2A28,
            "Un detective cruza la Patagonia siguiendo un rastro que desaparece.")

        val cartografos = movie("cartografos", "Los Cartógrafos", "Aventura", 0xFFE08A3A, 0xFF2A1306,
            "Dos exploradores dibujan un mapa que no deja de cambiar.")
        val jardin = movie("jardin-hielo", "El Jardín de Hielo", "Fantasía", 0xFF5FB4FF, 0xFF0D1F4A,
            "Una niña descubre un jardín que florece solo en invierno.")
        val vertigo = movie("vertigo-azul", "Vértigo Azul", "Suspenso", 0xFF7A5CFF, 0xFF150F4A,
            "Un buzo escucha una voz a cien metros de profundidad.")
        val ruta = movie("ruta-40", "Ruta 40", "Road movie", 0xFFFFB347, 0xFF3A1A0A,
            "Tres amigos, una camioneta y cinco mil kilómetros hasta el sur.")
        val umbria = movie("casa-umbria", "Casa Umbría", "Terror", 0xFFC04A8A, 0xFF1A0716,
            "Una mudanza, una puerta que no estaba y algo que espera adentro.")
        val eco = movie("eco-sal", "Eco de Sal", "Documental", 0xFF3AB8C8, 0xFF082026,
            "El último viaje de las salinas más grandes del país.")
        val ceniza = movie("ceniza-dorada", "Ceniza Dorada", "Histórica", 0xFFE0A63A, 0xFF3A210A,
            "1930: un joyero esconde su tesoro antes de que todo cambie.")
        val cielo = movie("cielo-roto", "Cielo Roto", "Acción", 0xFFD6455A, 0xFF22080F,
            "Una piloto debe aterrizar un avión sin torre de control.")

        val seriesAll = listOf(noche, frecuencia, costa, hermanas, polvo, cenizas, marea, sur)
        val moviesAll = listOf(cartografos, jardin, vertigo, ruta, umbria, eco, ceniza, cielo)
        val library = seriesAll + moviesAll

        val continuar = Rail(
            "Continuar viendo",
            listOf(
                noche.copy(progress = 0.62f), frecuencia.copy(progress = 0.30f), costa.copy(progress = 0.85f),
                hermanas.copy(progress = 0.12f), polvo.copy(progress = 0.45f), cenizas.copy(progress = 0.70f),
            ),
            wide = true,
        )
        val misterio = library.filter { m -> listOf("Misterio", "Suspenso", "Thriller").any { m.genre.contains(it) } }

        return HomeContent(
            featured = listOf(noche, cartografos, frecuencia, jardin, costa),
            rails = listOf(
                continuar,
                Rail("Series populares", seriesAll),
                Rail("Películas para ti", moviesAll),
                Rail("Misterio y suspenso", misterio),
            ),
            library = library,
        )
    }
}
