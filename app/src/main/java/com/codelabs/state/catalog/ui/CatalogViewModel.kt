package com.codelabs.state.catalog.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.repository.FavoritesRepository
import com.codelabs.state.catalog.domain.usecase.GetActivitiesPageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * TODO 6 - El ViewModel del catálogo (MVVM).
 *
 * Es el dueño del estado de la PANTALLA: búsqueda, categoría, actividades cargadas,
 * favoritos, borradas, loading / error / fin. Habla solo con el dominio: el caso de uso
 * para las páginas y la INTERFAZ FavoritesRepository para los favoritos. Nunca con data/. La consigna completa está en EJERCICIO.md.
 */
class CatalogViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getActivitiesPage: GetActivitiesPageUseCase,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    // ---------------------------------------------------------------------------
    // La UI lee de acá y de ningún otro lado. Los favoritos salen de favoritesRepository.
    //  (placeholder para que compile)
    // ---------------------------------------------------------------------------
    val uiState: StateFlow<CatalogUiState> = MutableStateFlow(CatalogUiState()).asStateFlow()

    // Al crearse carga la primera página.

    // ---------------------------------------------------------------------------
    // Búsqueda y categoría
    //   - reinician la paginación desde la página 0
    //   - una carga en curso de la búsqueda vieja no puede terminar mezclada en la nueva
    //   - sobreviven a que el sistema mate el proceso (SavedStateHandle)
    // ---------------------------------------------------------------------------
    fun onQueryChange(query: String) {
    }

    fun onCategoryChange(category: Category?) {
    }

    // ---------------------------------------------------------------------------
    // TODO 9 - Pide la página siguiente. Lo llama la UI cuando el scroll se acerca al final,
    //  y el botón "Reintentar".
    //   - nunca dos cargas a la vez, y nunca la misma página dos veces
    //   - después de la última página no pide nada más
    //   - si falla: hasError = true, y lo ya cargado se sigue viendo
    // ---------------------------------------------------------------------------
    fun loadNextPage() {
    }

    // Se ve al instante en todas las cards.
    fun onToggleFavorite(id: Int) {
    }

    // TODO 10 - Borrar y deshacer: deshacer la devuelve a SU MISMO lugar en la lista.
    fun onDelete(id: Int) {
    }

    fun onUndoDelete(id: Int) {
    }

    // TODO 6 - Factory: viewModel() no sabe construir esto solo (recibe un caso de uso y un repo).
    //  Armala con lo que hay en di/CatalogServiceLocator.
}
