package com.codelabs.state.catalog.data.repository

import com.codelabs.state.catalog.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

// ---------------------------------------------------------------------------
// TODO 5 - Repositorio de favoritos (capa de datos)
//
//  Los favoritos se guardan en memoria (no hay base de datos en este ejercicio).
//   - observeFavorites emite el set actual apenas alguien se suscribe, y de nuevo
//     cada vez que cambia
//   - los favoritos sobreviven aunque el ViewModel muera (ej. con "No conservar
//     actividades"): pensá cuántas instancias de este repo tiene que haber
// ---------------------------------------------------------------------------
class FavoritesRepositoryImpl : FavoritesRepository {

    override fun observeFavorites(): Flow<Set<Int>> = TODO("Punto 5")

    override suspend fun toggle(id: Int) {
        TODO("Punto 5")
    }
}
