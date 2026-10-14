# Ejercicio: Catálogo con Compose, MVVM y Clean Architecture

## De qué va

La pestaña **Catálogo** (cuarta de la app) es un catálogo de actividades de bienestar con
**infinite scroll**: las actividades llegan de a páginas desde un "backend" fake.

Esta vez armás la feature **completa, de punta a punta**, separada en capas:

```
ui/          Compose + CatalogViewModel          (MVVM)
  │  habla solo con ↓
domain/      modelos · interfaces de repositorio · caso de uso    (Kotlin puro)
  ↑  implementa las interfaces
data/        repositorios · API fake (DTOs)
di/          arma todo y se lo pasa al ViewModel
```

La regla de dependencias: **`domain` no conoce a nadie** (ni Android, ni Compose, ni
`data`). `data` y `ui` dependen de `domain`, nunca al revés. La UI no toca repositorios ni
la API: solo ve el ViewModel. El ViewModel solo ve el **dominio**: el caso de uso y la
interfaz `FavoritesRepository`. Nunca una clase de `data/`.

Arrancá corriendo la app y entrando a la pestaña Catálogo: vas a ver un texto que te manda
acá. **Ese es el punto de partida correcto**, no un bug.

### Qué está hecho y qué no

| Archivo | Estado |
|---|---|
| `domain/model/CatalogModels.kt` | ✅ dado: modelos de dominio |
| `domain/repository/*Repository.kt` | ✅ dado: los contratos (interfaces) |
| `data/remote/FakeCatalogApi.kt` | ✅ dado: la API fake, devuelve DTOs |
| `ui/CatalogUiState.kt` | ✅ dado: lo que la pantalla necesita para dibujarse |
| `ui/CategoryUi.kt` | ✅ dado: texto e ícono de cada categoría e intensidad |
| `domain/usecase/GetActivitiesPageUseCase.kt` | ✏️ **vos** (punto 4) |
| `data/repository/*RepositoryImpl.kt` | ✏️ **vos** (punto 5) |
| `di/CatalogServiceLocator.kt` | ✏️ **vos** (punto 6) |
| `ui/CatalogViewModel.kt` | ✏️ **vos** (punto 6) |
| `ui/CatalogTheme.kt` · `ui/CatalogScreen.kt` | ✏️ **vos** (puntos 1-3 y 7-11) |
| test del caso de uso | ✏️ **vos** (punto 4) |

Las firmas de las clases y de los composables chicos (`ActivityCard`, `CategoryChips`,
`SearchField`, `PagingFooter`) están dadas; los cuerpos no. La firma de `CatalogContent`
la diseñás vos.

### La API fake

`FakeCatalogApi.fetchActivities(page, categoryCode, search)` tarda 800 ms y devuelve un
`ActivitiesPageDto(results, nextPage)`.

- Las páginas son de 20 y arrancan en 0. Hay 300 actividades: sin filtros, la última es la 14.
- Habla "en API", no en dominio:
  - `categoryCode`: `"MIND"`, `"CARDIO"`, `"STRENGTH"`, `"OUTDOOR"`, o `null` para todas
  - `intensityLevel`: `1`, `2` o `3`
  - `nextPage`: el número de la siguiente, o `null` si no hay más
  - `search`: `null` es sin filtro
- Si la "red" falla, tira `IOException`.
- Los datos son determinísticos: el mismo id es siempre la misma actividad.

Palanca para probar el camino de error:
- `FakeCatalogApi.failOnPage = 3` → la página 3 falla **la primera vez** que se la pide.
  El reintento anda.

---

## Los 11 puntos

Cada uno tiene su `TODO` numerado en el archivo que corresponde.

### 1. Theme Material 3 (`CatalogTheme`)
Colores, tipografía y formas propias del catálogo.

**Criterios:**
- un esquema de colores para modo claro y otro para oscuro, sin dynamic color. Cambiar el
  modo oscuro del sistema cambia los colores de la pantalla
- tipografía propia: que se note en los títulos
- las cards usan la forma `medium` del theme
- en `CatalogScreen.kt` no queda ningún `Color(0x...)`: todos los colores salen del theme
- el theme envuelve solo esta pantalla: el resto de la app no cambia

### 2. `ActivityCard`: layout y modifiers
Ícono de la categoría dentro de un círculo, nombre, `"30 min · Alta"` y botón de favorito.
Expandida muestra además la descripción y un botón **Borrar**.

**Criterios:**
- tocar **en cualquier parte** de la card, incluido el borde, la expande. El ripple cubre
  la card entera
- el fondo del ícono es un círculo, sin esquinas cuadradas asomando
- el nombre usa todo el ancho que sobra y el favorito queda pegado a la derecha
- con un nombre largo, el texto baja de renglón; no pisa al favorito ni se corta
- en la grilla (punto 8) las celdas son angostas: el nombre **no se corta a mitad de
  palabra**. Puede que ahí la card necesite otro layout (y la firma, otro parámetro)

### 3. Previews
**Criterios:**
- cada composable stateless tiene su `@Preview`, en modo claro **y** oscuro, con datos fake
- la card se puede ver colapsada y expandida en el panel de previews sin correr la app
- hay una preview de la pantalla entera (`CatalogContent`) **sin ViewModel**. Si no la
  podés armar, revisá el punto 7

### 4. Dominio: caso de uso
`GetActivitiesPageUseCase`: traer una página del catálogo aplicando las reglas de negocio.

Los favoritos no tienen caso de uso a propósito: marcar y desmarcar no tiene ninguna regla,
y un caso de uso que solo delega al repo es una capa sin trabajo. El ViewModel usa la
interfaz `FavoritesRepository` directo (pregunta 17).

**Criterios:**
- hace **una** cosa y se llama como una función (`getActivitiesPage(0, null, "")`)
- depende de la **interfaz** `ActivitiesRepository`, nunca de `ActivitiesRepositoryImpl`
- **regla de negocio** en `GetActivitiesPageUseCase`: la búsqueda por nombre arranca con
  **2 letras**. La query se usa recortada (sin espacios al principio ni al final); con menos
  de 2 letras se busca sin filtro de nombre. En la app: escribir "p" sigue mostrando todo,
  "pl" ya filtra
- **test unitario** de `GetActivitiesPageUseCase` en `app/src/test/java/com/codelabs/state/catalog/`,
  con un repositorio fake (sin red, sin Android). Mínimo: una letra, query con espacios,
  y que un error del repo llegue como error. Corre con `./gradlew :app:testDebugUnitTest`
- en `domain/` no hay ningún `import android...`, `androidx...` ni `com.codelabs.state.R`

### 5. Datos: repositorios
`ActivitiesRepositoryImpl` y `FavoritesRepositoryImpl`, que implementan las interfaces del
dominio.

**Criterios:**
- `ActivitiesRepositoryImpl` convierte **DTO → dominio** (código de categoría, nivel de
  intensidad, `nextPage` → `hasMore`) y **dominio → API** (`Category` → código, `""` → `null`).
  Fuera de `data/` nadie sabe que existen los DTOs ni los códigos
- un error de red vuelve como `Result.failure`, no como excepción
- **cancelar la carga sigue cancelando** (ver hints): cambiar la búsqueda en medio de una
  carga no muestra un error
- `FavoritesRepositoryImpl` guarda en memoria. `observeFavorites()` emite el valor actual
  apenas alguien se suscribe y de nuevo en cada cambio
- marcá favoritos, activá **"No conservar actividades"** (Opciones de desarrollador), salí de
  la app y volvé: los favoritos **siguen ahí** (el ViewModel murió, el repo no)

### 6. ViewModel (MVVM) y armado de dependencias
`CatalogViewModel` es el dueño del estado de la **pantalla**: búsqueda, categoría,
actividades cargadas, favoritos, borradas, cargando / error / fin.

**Criterios:**
- expone **un solo** `StateFlow<CatalogUiState>`. La UI no lee nada más
- recibe el caso de uso y `FavoritesRepository` **por constructor**: no crea nada él
  mismo. Los arma `di/CatalogServiceLocator`, y `viewModel()` los recibe con una **Factory**
- habla solo con el dominio: ningún `import` de `data/` en el ViewModel
- al crearse carga la primera página
- **girar el teléfono no recarga nada**: no aparece el spinner, la lista sigue donde estaba
- cambiar la búsqueda o la categoría reinicia la paginación desde la página 0
- con "No conservar actividades": salir y volver mantiene **la búsqueda y la categoría**
  (el ViewModel es nuevo; lo que sobrevive es lo que guardaste en `SavedStateHandle`)
- los favoritos se ven marcados **al instante**, en todas las cards

### 7. Estado en la UI: hoisting y UDF
**Criterios:**
- `CatalogScreen` es el único composable que conoce al ViewModel: lee `uiState` y pasa
  **valores** para abajo; los eventos suben como **lambdas** y terminan en funciones del
  ViewModel
- `CatalogContent` y todo lo que está debajo son **stateless** y no conocen al ViewModel
- **no todo va al ViewModel**: el modo lista/grilla, la posición del scroll y "esta card
  está expandida" son estado de UI y viven en Compose. También sobreviven a girar el teléfono
- escribir en la búsqueda no hace "saltar" el cursor ni pierde letras

### 8. Listas lazy y `key`
Chips de categoría que scrollean horizontal ("Todas" + una por categoría), la lista de
actividades y, con un botón en la TopAppBar, la misma lista en **grilla**.

**Criterios:**
- con 300 actividades cargadas, el scroll va fluido
- la grilla acomoda la cantidad de columnas según el ancho (girá el teléfono: tiene que
  haber más columnas en horizontal)
- **la prueba del `key`**: expandí una card, borrá la que está justo arriba. La expandida
  tiene que seguir siendo **la misma actividad**, no pasar a la de al lado
- cuando llega una página nueva, el scroll no "salta"

### 9. Infinite scroll
Cuando faltan pocos ítems para llegar al final (unos 5), se pide la página siguiente. Sin
botón de "cargar más": lo dispara el scroll. La UI **avisa** ("estoy cerca del final"); qué
página pedir y si corresponde pedirla lo decide el **ViewModel**.

**Criterios:**
- footer con spinner mientras carga
- al final de todo, footer "No hay más actividades" y **no se vuelve a pedir nada**
- si el filtro no trae nada: "Ninguna actividad coincide con la búsqueda"
- **nunca se pide la misma página dos veces**. Scrolleá como loco contra el borde. Si se
  duplica una página la app crashea por `key` repetidas, así que te vas a enterar
- funciona igual en lista y en grilla. En grilla el footer ocupa todo el ancho
- con `failOnPage = 3`: footer de error con **Reintentar**, y lo que ya estaba cargado se
  sigue viendo. Reintentar trae la página y la carga sigue normal
- **escribir rápido en la búsqueda** ("pla" → "plan" → "plancha") no deja resultados de una
  búsqueda vieja mezclados con los de la nueva, ni un spinner colgado

### 10. Scaffold, componentes de Material y "Deshacer"
**Criterios:**
- `TopAppBar` con título y botón lista/grilla. Se esconde al scrollear para abajo y vuelve
  al scrollear para arriba
- FAB **"Volver arriba"** que aparece solo si scrolleaste, y te lleva arriba animado
- **borrar** una actividad muestra un snackbar con **Deshacer**, y Deshacer la devuelve
  **a su mismo lugar**. Qué está borrado lo sabe el ViewModel; el snackbar es cosa de la UI
- si la restaurada queda arriba, fuera de pantalla, la lista la trae a la vista
- la TopAppBar no queda con un hueco del alto de la status bar arriba (ver hints)

### 11. Animaciones y accesibilidad
**Animaciones:**
- expandir y contraer una card anima el alto
- borrar, deshacer y filtrar animan los ítems que entran, salen y se mueven
- el FAB aparece y desaparece animado
- el corazón del favorito cambia de color animado

**Accesibilidad (probalo con TalkBack prendido):**
- TalkBack lee la card **como una sola cosa** ("Plancha #11, 30 min · Alta") y dice
  "doble toque para **expandir**" o "para **contraer**", no un "activar" genérico
- el favorito se anuncia como "Favorito" y dice si está **marcado o no**
- los íconos decorativos (el de la categoría, la lupa) no se leen
- todo lo tocable mide al menos 48dp
- con el tamaño de fuente del sistema al máximo, no se corta ni se superpone ningún texto
  (nada de altos fijos en lo que tiene texto)

---

## Hints

*No mirar antes de intentarlo.* Estas son las herramientas que entran en juego, sin decir
dónde va cada una:

**Arquitectura:** `operator fun invoke` · `Result` / `onSuccess` / `onFailure` ·
`try`/`catch` · `object` + `by lazy` · `ViewModelProvider.Factory` / `viewModelFactory { initializer { … } }` ·
`createSavedStateHandle()` · `SavedStateHandle` · `viewModelScope` · `Job.cancel()` ·
`MutableStateFlow.update` · `combine` · `stateIn` · `SharingStarted.WhileSubscribed` ·
`runBlocking` (en el test)

**Compose:** `Row` / `Column` / `Box` · `Spacer` · `Modifier.weight` · `clip` ·
`clickable(onClickLabel = …)` · `remember` · `rememberSaveable` · `mutableStateOf` ·
`collectAsStateWithLifecycle` · `viewModel(factory = …)` · `derivedStateOf` · `snapshotFlow` ·
`LaunchedEffect(keys…)` · `rememberCoroutineScope` · `LazyColumn` / `LazyRow` / `LazyVerticalGrid` ·
`GridCells.Adaptive` · `items(key = …)` · `item(span = …)` · `rememberLazyListState` /
`rememberLazyGridState` · `layoutInfo.visibleItemsInfo` · `requestScrollToItem` ·
`MaterialTheme` · `lightColorScheme` / `darkColorScheme` · `Typography` · `Shapes` ·
`Scaffold` · `TopAppBar` · `TopAppBarDefaults.enterAlwaysScrollBehavior` · `Modifier.nestedScroll` ·
`SnackbarHostState` · `FilterChip` · `OutlinedTextField` · `IconToggleButton` ·
`animateContentSize` · `Modifier.animateItem` · `AnimatedVisibility` · `animateColorAsState` ·
`@Preview(uiMode = …)`

Detalles que te van a morder:

**Arquitectura**
- **`runCatching` atrapa todo, incluida la `CancellationException`.** Si el ViewModel
  cancela una carga porque cambió la búsqueda, `runCatching` la convierte en un error
  "normal" y la pantalla muestra "No se pudo cargar" de algo que nadie está esperando.
  Atrapá solo lo que corresponde.
- **un `finally { isLoading = false }` en la carga** parece prolijo, pero cuando esa carga
  se cancela, el `finally` corre **después** de que la carga nueva puso `isLoading = true`,
  y lo pisa. Resultado: la carga nueva sin spinner.
- `viewModel()` sabe construir ViewModels sin parámetros (o solo con `SavedStateHandle`). El
  tuyo recibe un caso de uso y un repo: sin una Factory, crashea al abrir la pantalla.
- si cada ViewModel crea **su propio** `FavoritesRepositoryImpl`, los favoritos se pierden
  cuando el ViewModel muere. Cuántas instancias hay de cada cosa lo decide el service locator.
- `SavedStateHandle` guarda lo mismo que un `Bundle`: `String`, enums, números, arrays...
  No guardes ahí la lista de actividades (no entra, y no hace falta: se vuelve a pedir).
- `_state.value = _state.value.copy(...)` y `_state.update { it.copy(...) }` no son lo mismo
  cuando hay varias corrutinas escribiendo. Usá `update`.

**Compose**
- **el Scaffold va adentro de otro Scaffold** (el de `MainActivity`), que ya descontó la
  status bar. Si no le decís nada, la tuya la descuenta de nuevo y queda un hueco arriba.
  Mirá `contentWindowInsets` del Scaffold y `windowInsets` de la TopAppBar.
- `remember { mutableStateOf(mutableSetOf<Int>()) }` y después `set.add(id)` **no recompone**:
  el State sigue apuntando al mismo objeto. Lo mismo con un `MutableStateFlow` de una lista
  mutable. Usá valores inmutables y reasigná.
- leer `listState.layoutInfo` directo en el cuerpo de un composable **recompone en cada
  frame del scroll**. Hay que leerlo en algún lugar que solo reaccione cuando cambia el
  resultado.
- "estoy cerca del final" puede **seguir siendo verdad** después de cargar una página
  (filtro con pocos resultados, pantalla grande, grilla). Si tu trigger solo dispara cuando
  ese valor *cambia*, la carga se queda trabada.
- **la lista se ancla al primer ítem visible por su `key`.** Si cuando llega la primera
  página lo único que había en la lista era el footer de "cargando", la lista lo sigue
  y salta hasta abajo (y eso pide otra página, y otra...). Con la lista vacía, el
  "cargando" no va adentro de la lista. Y por lo mismo: buscá "plancha", scrolleá un poco
  y borrá la búsqueda. Si la lista nueva arranca en "Plancha #7" y no arriba de todo, es
  el ancla. Mirá la diferencia entre `scrollToItem` y `requestScrollToItem`.
- `stringResource` no se puede llamar adentro de un `onClick` ni de un `launch`: no son
  `@Composable`. Para el texto del snackbar resolvelo antes, o usá `LocalResources`.

---

### Arquitectura
16. ¿Qué responsabilidad tiene cada capa (`ui`, `domain`, `data`) y por qué `domain` no
    puede depender de Android?
17. ¿Qué gana la app con que el ViewModel dependa de un caso de uso, y el caso de uso de
    una interfaz, en vez de usar directamente la API? Respondé con tu test como ejemplo.
    ¿Por qué los favoritos no tienen caso de uso, y cuándo convendría crearles uno?
18. En tu pantalla, ¿qué estado quedó en el ViewModel y cuál en Compose? ¿Con qué criterio
    lo decidiste?
19. ¿Qué diferencia hay entre lo que sobrevive a girar el teléfono y lo que sobrevive a que
    el sistema mate el proceso? ¿Qué usaste para cada caso?
