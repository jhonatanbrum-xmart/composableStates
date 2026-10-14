package com.codelabs.state.catalog.data.repository

import com.codelabs.state.catalog.data.remote.FakeCatalogApi
import com.codelabs.state.catalog.domain.model.ActivitiesPage
import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.repository.ActivitiesRepository

// ---------------------------------------------------------------------------
// TODO 5 - Repositorio de actividades (capa de datos)
//
//  Le pide la página a FakeCatalogApi y la devuelve en modelos de DOMINIO.
//   - de DTO a dominio: categoryCode -> Category, intensityLevel -> Intensity,
//     nextPage -> hasMore. Del dominio a la API: Category -> categoryCode,
//     query "" -> search null
//   - un error de red vuelve como Result.failure, no como excepción
//   - cancelar la corrutina que lo llama tiene que seguir cancelándola
//     (ojo con qué atrapás)
// ---------------------------------------------------------------------------
class ActivitiesRepositoryImpl(
    private val api: FakeCatalogApi = FakeCatalogApi,
) : ActivitiesRepository {

    override suspend fun getPage(page: Int, category: Category?, query: String): Result<ActivitiesPage> =
        TODO("Punto 5")
}
