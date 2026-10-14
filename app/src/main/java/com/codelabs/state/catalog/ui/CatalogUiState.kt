package com.codelabs.state.catalog.ui

import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.model.WellnessActivity

/** Todo lo que la pantalla necesita para dibujarse. Lo expone CatalogViewModel. Ya está hecho. */
data class CatalogUiState(
    val query: String = "",
    val category: Category? = null,
    /** Lo cargado hasta ahora, SIN las actividades borradas. */
    val activities: List<WellnessActivity> = emptyList(),
    val favorites: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val hasError: Boolean = false,
)
