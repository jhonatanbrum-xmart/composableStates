package com.codelabs.state.catalog.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// ---------------------------------------------------------------------------
// TODO 1 - Theme Material 3 propio del catalogo
//
//  Hoy esto no hace nada: MaterialTheme sin parametros hereda el theme de afuera.
//  Tiene que tener colores, tipografia y formas propias:
//
//   - un esquema de colores para modo claro y otro para modo oscuro (sin dynamic
//     color, para que se vea que es TU theme); darkTheme ya te dice cual usar
//   - una tipografia propia (aunque sea cambiar la familia de los titulos)
//   - formas propias: las cards usan la forma "medium" del theme
//
//  Cuando este hecho, en CatalogScreen.kt no tiene que quedar ningun Color(0x...):
//  todos los colores se piden al theme.
// ---------------------------------------------------------------------------
@Composable
fun CatalogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(content = content)
}
