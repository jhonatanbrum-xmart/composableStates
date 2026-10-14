package com.codelabs.state.catalog.data.remote

import kotlinx.coroutines.delay
import java.io.IOException

/*
 * "Backend" fake y paginado del catálogo. Ya está hecho: no hay nada para tocar acá
 * (salvo la palanca failOnPage para probar el camino de error).
 *
 * Habla en DTOs, como una API de verdad: los nombres y tipos son los del "JSON",
 * no los del dominio. Convertirlos es trabajo del repositorio.
 */

data class ActivityDto(
    val id: Int,
    val title: String,
    /** "MIND", "CARDIO", "STRENGTH" u "OUTDOOR" */
    val categoryCode: String,
    val minutes: Int,
    /** 1 = baja, 2 = media, 3 = alta */
    val intensityLevel: Int,
    val description: String,
)

data class ActivitiesPageDto(
    val results: List<ActivityDto>,
    /** La página siguiente, o null si esta era la última. */
    val nextPage: Int?,
)

object FakeCatalogApi {

    const val PAGE_SIZE = 20
    private const val TOTAL_ACTIVITIES = 300
    private const val NETWORK_DELAY_MS = 800L

    /**
     * Palanca de error: si se pone, esa página (0-based) falla la PRIMERA vez que se la pide
     * (tira IOException). El reintento anda. Ej: `FakeCatalogApi.failOnPage = 3`
     */
    var failOnPage: Int? = null
    private val alreadyFailed = mutableSetOf<Int>()

    private val all: List<ActivityDto> by lazy { List(TOTAL_ACTIVITIES, ::activity) }

    /**
     * Una página (0-based) del resultado filtrado. Tarda ~800 ms.
     *
     * @param categoryCode null = todas
     * @param search null = sin filtro; si viene, filtra por título sin importar mayúsculas
     * @throws IOException si la "red" falla
     */
    suspend fun fetchActivities(page: Int, categoryCode: String?, search: String?): ActivitiesPageDto {
        delay(NETWORK_DELAY_MS)
        if (page == failOnPage && alreadyFailed.add(page)) {
            throw IOException("No se pudo cargar la pagina $page")
        }
        val matching = all.filter {
            (categoryCode == null || it.categoryCode == categoryCode) &&
                (search == null || it.title.contains(search, ignoreCase = true))
        }
        val hasMore = (page + 1) * PAGE_SIZE < matching.size
        return ActivitiesPageDto(
            results = matching.drop(page * PAGE_SIZE).take(PAGE_SIZE),
            nextPage = if (hasMore) page + 1 else null,
        )
    }

    private val CODES = listOf("MIND", "CARDIO", "STRENGTH", "OUTDOOR")

    private fun activity(id: Int): ActivityDto {
        val code = CODES[id % CODES.size]
        val names = NAMES.getValue(code)
        return ActivityDto(
            id = id,
            title = "${names[(id / CODES.size) % names.size]} #${id + 1}",
            categoryCode = code,
            minutes = 5 + (id * 7) % 56,
            intensityLevel = (id / 3) % 3 + 1,
            description = DESCRIPTIONS.getValue(code),
        )
    }

    private val NAMES = mapOf(
        "MIND" to listOf(
            "Respiración 4-7-8", "Meditación guiada", "Escaneo corporal",
            "Diario de gratitud", "Comer con atención",
        ),
        "CARDIO" to listOf("Trote suave", "Saltar la soga", "Bici fija", "Baile libre", "Subir escaleras"),
        "STRENGTH" to listOf("Sentadillas", "Plancha", "Flexiones", "Estocadas", "Puente de glúteos"),
        "OUTDOOR" to listOf("Caminata en el parque", "Senderismo", "Yoga al aire libre", "Remo", "Jardinería"),
    )

    private val DESCRIPTIONS = mapOf(
        "MIND" to "Buscá un lugar tranquilo, sentate cómodo y llevá la atención a la respiración. " +
            "Si la mente se va, volvé sin juzgarte.",
        "CARDIO" to "Arrancá con 3 minutos de entrada en calor y mantené un ritmo en el que puedas " +
            "hablar pero no cantar. Hidratate al terminar.",
        "STRENGTH" to "Priorizá la técnica antes que las repeticiones. Hacé 3 series con un minuto " +
            "de descanso entre cada una.",
        "OUTDOOR" to "Aprovechá la luz del día y elegí un recorrido conocido. Llevá agua y protector " +
            "solar aunque esté nublado.",
    )
}
