# Ejercicio: corrutinas y Flows en el Dashboard

## De qué va

La pantalla **Dashboard** (tercera pestaña de la app) ya está hecha, y los repos fake también.
Lo único vacío es `DashboardViewModel`: las firmas están, los cuerpos no.
Todo el trabajo en background va en `viewModelScope`.

Arrancá corriendo la app y entrando a la pestaña Dashboard: vas a ver un spinner
que nunca termina. **Ese es el punto de partida correcto**, no un bug.

### Qué NO hay que tocar

- `DashboardScreen.kt` — la UI está completa
- `data/DashboardRepositories.kt` — los repos están completos
- `DashboardModels.kt` — los modelos están completos

Todo el ejercicio pasa en `DashboardViewModel.kt`.

### La solución

Está en `solucion/DashboardViewModelSolucion.kt`, comentada punto por punto.
**No la mires antes de intentarlo.** No la usa nadie: la pantalla sigue apuntando a
`DashboardViewModel`. Y no es *la* solución, es *una*: varios puntos tienen más de una
salida válida.

### Los repos que tenés a mano

| Llamada | Tarda | Devuelve |
|---|---|---|
| `userRepository.getUser()` | 800 ms | `User` |
| `weatherRepository.getWeather(city)` | 1200 ms | `Weather` |
| `notificationsRepository.getNotifications()` | 1500 ms | `List<Notification>` |
| `notificationsRepository.markAsRead(id)` | 300 ms | nada (tira excepción si `id == 4`) |
| `notificationsRepository.liveNotifications()` | cada 5 s | `Flow<Notification>` infinito |
| `statsRepository.computeEngagementScore(list)` | ~0,5 s **bloqueando el hilo** | `Int` |

Dos palancas para probar los caminos de error:
- `weatherRepository.shouldFail = true` → el clima falla
- tocar la notificación **"3 tareas vencen mañana"** (id 4) → `markAsRead` falla

---

## Los 7 puntos

Cada uno tiene su `TODO` numerado en `DashboardViewModel.kt`.

### 1. Estado único (`uiState`)
La UI lee de `uiState` y de ningún otro lado. Tiene que juntar las 7 fuentes internas
(`_user`, `_weather`, `_notifications`, `_filter`, `_engagementScore`, `_isLoading`, `_error`)
en un solo `DashboardUiState`.

**Criterios:**
- la lista que llega a la UI ya viene **filtrada** según el filtro activo
- `unreadCount` cuenta las no leídas **del total**, no de la lista filtrada
- girar el teléfono no vuelve a arrancar nada de cero
- si nadie está mirando la pantalla, esto no sigue recalculándose

### 2. Carga inicial
Al abrir la pantalla se cargan los datos y se empieza a escuchar el flujo live.

### 3. `refresh()`
Trae usuario, clima y notificaciones.

**Criterios:**
- **el refresh completo tarda ~1,5 s, no ~3,5 s.** Cronometralo: si tarda 3,5 estás
  esperando una cosa atrás de la otra
- mientras carga se ve el spinner; cuando termina desaparece, **aunque algo haya fallado**
- con `weatherRepository.shouldFail = true`: se ve el error **y además** se siguen viendo
  el usuario y las notificaciones, que sí llegaron. Ojo acá: usar `viewModelScope` no
  alcanza para que las otras dos cargas sobrevivan
- tocar refresh dos veces seguidas no deja la pantalla en un estado raro

### 4. Score de engagement
`computeEngagementScore()` bloquea el hilo desde el que se lo llame por medio segundo.

**Criterios:**
- se recalcula cada vez que cambia la lista de notificaciones y termina visible en la UI
- **la lista scrollea fluido mientras el cálculo corre.** Si se traba, lo estás corriendo
  donde no va
- si las notificaciones cambian de nuevo antes de que termine, el score que queda es el
  de la lista más nueva, no el de la vieja (probalo tocando dos notificaciones seguidas)

### 5. `onFilterChanged()`
Cambia lo que se ve al instante y **no dispara ninguna llamada a los repos**
(se verifica: al cambiar de chip no aparece el spinner).

### 6. `markAsRead()`
**Criterios:**
- el item se ve leído **apenas lo tocás**, sin esperar los 300 ms del repo
- si el repo falla (id 4), vuelve a verse como no leída y se muestra el error
- marcar como leída no puede pisar cambios que hayan pasado mientras tanto

### 7. Notificaciones live
`liveNotifications()` emite una cada 5 s y nunca termina.

**Criterios:**
- cada una aparece **arriba de todo** en la lista, sin tocar nada
- se sigue escuchando mientras el ViewModel viva (cortarlo cuando muere sale gratis:
  `viewModelScope` se cancela solo, no hay que escribir nada)

> Ojo con un detalle: el ViewModel está scopeado a la Activity, así que **cambiar de pestaña
> no lo mata**. Volver al Dashboard no re-fetchea nada y el flujo live siguió corriendo.
> Recién se cancela al cerrar la app de verdad.

---

## Hints

*No mirar antes de intentarlo.* Estas son las herramientas que entran en juego, sin decir
dónde va cada una:

`viewModelScope` · `launch` · `async` / `await` · `withContext` · `Dispatchers.Default` ·
`combine` · `stateIn` · `SharingStarted.WhileSubscribed` · `onEach` · `mapLatest` · `launchIn` ·
`Job.cancel()` · `MutableStateFlow.update` · `runCatching` / `try-catch`

Tres detalles que te van a morder:

- `combine` tiene overloads hasta **5** flows. Vos tenés 7. Hay más de una salida
  (anidar, el overload de `vararg`, o agrupar fuentes); cualquiera vale.
- `_notifications.value = lista` y `_notifications.update { ... }` no son lo mismo cuando
  hay varias corrutinas escribiendo al mismo tiempo. Los puntos 6 y 7 se pisan entre sí.
- si uno de los tres `async` del punto 3 tira excepción, se lleva puestos a los hermanos.
  La jerarquía que manda ahí es la del `launch` que los contiene, no la del scope.
