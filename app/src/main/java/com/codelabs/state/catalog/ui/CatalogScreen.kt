package com.codelabs.state.catalog.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codelabs.state.R
import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.model.WellnessActivity

/**
 * EJERCICIO DE COMPOSE UI, ESTADO, MVVM Y CLEAN ARCHITECTURE.
 *
 * La consigna completa, con los criterios de aceptación, está en EJERCICIO.md (paquete
 * catalog). Los TODO dicen QUÉ tiene que pasar, no CÓMO.
 *
 * Arranca compilando y corriendo: la pestaña Catálogo muestra un texto que te manda a
 * EJERCICIO.md. Ese es el punto de partida correcto.
 */

// ---------------------------------------------------------------------------
// TODO 7 - La pantalla (stateful)
//
//  Obtiene el CatalogViewModel, lee su uiState y se lo pasa a CatalogContent como
//  valores; los eventos de la UI van a funciones del ViewModel.
//  El estado que es SOLO de la UI (modo lista/grilla, scroll, snackbar) no va al
//  ViewModel: vive acá, en Compose, y también sobrevive a girar el teléfono.
//
// TODO 9 - Infinite scroll (lado UI)
//  Cuando faltan pocos ítems para llegar al final, se le pide la página siguiente
//  al ViewModel. Tiene que andar en lista y en grilla.
//
// TODO 10 - Borrar con "Deshacer"
//  Borrar muestra un snackbar con "Deshacer". Si la restaurada queda fuera de
//  pantalla, la lista la trae a la vista.
//
//  (placeholder para que compile)
// ---------------------------------------------------------------------------
@Composable
fun CatalogScreen(modifier: Modifier = Modifier) {
    CatalogTheme {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.catalog_start_here))
        }
    }
}

// ---------------------------------------------------------------------------
// TODO 7 - CatalogContent (stateless)
//
//  Todo lo que se ve de la pantalla. No conoce al ViewModel: recibe valores y avisa
//  eventos con lambdas. La firma la diseñás vos.
//
// TODO 10 - Scaffold
//  TopAppBar (título + botón lista/grilla, se esconde al scrollear para abajo),
//  SnackbarHost, y un FAB "volver arriba" que aparece solo si scrolleaste.
//
// TODO 8 - Las listas
//  Lista de actividades y, con el botón de la TopAppBar, la misma en grilla.
//  Cada card se expande al tocarla. Expandí una, borrá la de arriba: la expandida
//  tiene que seguir siendo la misma actividad.
// ---------------------------------------------------------------------------

// ---------------------------------------------------------------------------
// TODO 2 - La card de una actividad
//  Ícono de la categoría en un círculo, nombre, "30 min · Alta", y botón de favorito.
//  Expandida muestra además la descripción y un botón "Borrar".
//   - tocar en cualquier parte de la card (incluido el borde) la expande
//   - el fondo del ícono es un círculo, sin esquinas
//  (ui/CategoryUi.kt ya tiene el texto y el ícono de cada categoría e intensidad)
//
// TODO 11 - Animaciones y accesibilidad (aplica acá y en el resto de la pantalla)
//   - expandir/contraer, borrar, filtrar y el FAB se animan; el favorito cambia de color animado
//   - TalkBack lee la card como una sola cosa y el favorito dice si está marcado
// ---------------------------------------------------------------------------
@Composable
fun ActivityCard(
    activity: WellnessActivity,
    isFavorite: Boolean,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
}

// ---------------------------------------------------------------------------
// TODO 8 - Chips de categoría: "Todas" + una por Category. Scrollean horizontal.
// ---------------------------------------------------------------------------
@Composable
fun CategoryChips(
    selected: Category?,
    onSelected: (Category?) -> Unit,
    modifier: Modifier = Modifier,
) {
}

// ---------------------------------------------------------------------------
// TODO 7 - Campo de búsqueda. Escribir no hace "saltar" el cursor.
// ---------------------------------------------------------------------------
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
}

// ---------------------------------------------------------------------------
// TODO 9 - El footer de la lista: cargando / error con "Reintentar" / no hay más
//  (o "ninguna actividad coincide" si la lista quedó vacía).
// ---------------------------------------------------------------------------
@Composable
fun PagingFooter(
    isLoading: Boolean,
    endReached: Boolean,
    hasError: Boolean,
    isEmpty: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
}

// ---------------------------------------------------------------------------
// TODO 3 - Previews
//  Cada composable stateless tiene su @Preview, en modo claro y oscuro, con datos fake.
// ---------------------------------------------------------------------------
