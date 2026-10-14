package com.codelabs.state.catalog.domain.repository

import com.codelabs.state.catalog.domain.model.ActivitiesPage
import com.codelabs.state.catalog.domain.model.Category

/**
 * Contrato del repositorio de actividades. Lo define el dominio; lo implementa la capa de datos
 * (data/repository/ActivitiesRepositoryImpl.kt).
 */
interface ActivitiesRepository {

    /**
     * Una página (0-based) del catálogo, ya en modelos de dominio.
     * Un error de red NO se tira como excepción: vuelve como Result.failure.
     *
     * @param category null = todas las categorías
     * @param query "" = sin filtro por nombre
     */
    suspend fun getPage(page: Int, category: Category?, query: String): Result<ActivitiesPage>
}
