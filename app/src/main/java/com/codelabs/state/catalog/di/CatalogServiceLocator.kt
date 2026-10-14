package com.codelabs.state.catalog.di

// ---------------------------------------------------------------------------
// TODO 6 - Armado de dependencias (DI manual, sin Hilt)
//
//  Acá se crean los repositorios y el caso de uso que necesita CatalogViewModel.
//   - el ViewModel recibe todo por constructor: no crea nada él mismo
//   - lo que se expone son las INTERFACES del dominio, no las *Impl
//   - ¿qué tiene que ser una única instancia para toda la app y qué puede crearse
//     cada vez? (pista: los favoritos)
// ---------------------------------------------------------------------------
object CatalogServiceLocator
