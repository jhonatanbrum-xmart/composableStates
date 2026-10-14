package com.codelabs.state.catalog.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de favoritos. Lo define el dominio; lo implementa la capa de datos
 * (data/repository/FavoritesRepositoryImpl.kt).
 */
interface FavoritesRepository {

    /** Los ids de las actividades favoritas. Emite de nuevo cada vez que cambian. */
    fun observeFavorites(): Flow<Set<Int>>

    /** Si era favorita deja de serlo, y viceversa. */
    suspend fun toggle(id: Int)
}
