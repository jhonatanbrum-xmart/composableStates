package com.codelabs.state.catalog.domain.usecase

import com.codelabs.state.catalog.domain.model.ActivitiesPage
import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.repository.ActivitiesRepository

// ---------------------------------------------------------------------------
// TODO 4 - Caso de uso: traer una página del catálogo
//
//  Regla de negocio: la búsqueda por nombre arranca recién con 2 letras.
//   - la query se usa sin espacios al principio ni al final
//   - con menos de 2 letras (después de recortar) se busca SIN filtro de nombre
//  Tiene test unitario (ver EJERCICIO.md).
// ---------------------------------------------------------------------------
class GetActivitiesPageUseCase(
    private val repository: ActivitiesRepository,
) {
    suspend operator fun invoke(page: Int, category: Category?, query: String): Result<ActivitiesPage> =
        TODO("Punto 4")
}
