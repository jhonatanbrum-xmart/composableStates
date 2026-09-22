package com.codelabs.state.dashboard

import androidx.lifecycle.ViewModel
import com.codelabs.state.dashboard.data.NotificationsRepository
import com.codelabs.state.dashboard.data.StatsRepository
import com.codelabs.state.dashboard.data.UserRepository
import com.codelabs.state.dashboard.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * EJERCICIO DE CORRUTINAS Y FLOWS.
 *
 * Los repos y la pantalla ya estan hechos. Todo lo que falta esta aca abajo.
 * La consigna completa, con los criterios de aceptacion, esta en EJERCICIO.md
 * (mismo paquete). Los TODO de este archivo dicen QUE tiene que pasar, no COMO.
 *
 * Arranca compilando y corriendo: la pantalla abre y se queda en loading para siempre.
 * Ese es el punto de partida correcto.
 *
 * Todo el trabajo en background va en viewModelScope.
 */
class DashboardViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val weatherRepository = WeatherRepository()
    private val notificationsRepository = NotificationsRepository()
    private val statsRepository = StatsRepository()

    // Fuentes de verdad internas. La UI no las ve: solo ve uiState.
    private val _user = MutableStateFlow<User?>(null)
    private val _weather = MutableStateFlow<Weather?>(null)
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    private val _filter = MutableStateFlow(NotificationFilter.All)
    private val _engagementScore = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    // ---------------------------------------------------------------------------
    // TODO 1 - Estado unico de la pantalla
    //
    //  La UI lee de aca y de ningun otro lado. Tiene que reflejar siempre la ultima
    //  version de: usuario, clima, notificaciones, filtro activo, score, loading y error.
    //
    //   - la lista de notificaciones que llega a la UI ya viene filtrada segun el
    //     filtro activo
    //   - unreadCount cuenta las NO leidas del total, no de la lista filtrada
    //   - si nadie esta mirando la pantalla, no tiene sentido que esto siga
    //     recalculandose; pero girar el telefono no tiene que volver a arrancar
    //     todo de cero
    //
    //  (placeholder para que compile)
    // ---------------------------------------------------------------------------
    val uiState: StateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState()).asStateFlow()

    init {
        // TODO 2 - Al abrir la pantalla se cargan los datos y se empieza a escuchar
        //  las notificaciones que llegan solas.
    }

    // ---------------------------------------------------------------------------
    // TODO 3 - Refresh (tambien es la carga inicial)
    //
    //  Trae usuario, clima y notificaciones. Las tres fuentes son independientes
    //  entre si: el refresh completo NO puede tardar la suma de las tres.
    //
    //   - mientras carga, la pantalla muestra loading; cuando termina, deja de mostrarlo
    //     (aunque haya fallado algo)
    //   - si una fuente falla, se muestra el error, pero lo que si llego se sigue viendo,
    //     y las otras dos cargas no se mueren con ella
    //   - tocar refresh dos veces seguidas no puede dejar la pantalla en un estado raro
    // ---------------------------------------------------------------------------
    fun refresh() {
    }

    // ---------------------------------------------------------------------------
    // TODO 4 - Score de engagement
    //
    //  Se recalcula cada vez que cambia la lista de notificaciones y termina en la UI.
    //  statsRepository.computeEngagementScore() es pesado y bloqueante (~medio segundo).
    //
    //   - la lista tiene que seguir scrolleando fluido mientras el calculo corre
    //   - si las notificaciones cambian de nuevo antes de que termine, el score que
    //     queda al final es el de la lista mas nueva, no el de la vieja
    // ---------------------------------------------------------------------------
    private fun recalculateScore(notifications: List<Notification>) {
    }

    // ---------------------------------------------------------------------------
    // TODO 5 - Cambiar el filtro
    //  Cambia lo que se ve al instante. No dispara ninguna llamada a los repos.
    // ---------------------------------------------------------------------------
    fun onFilterChanged(filter: NotificationFilter) {
    }

    // ---------------------------------------------------------------------------
    // TODO 6 - Marcar una notificacion como leida
    //
    //   - el item se ve leido apenas se lo toca, sin esperar la respuesta del repo
    //   - si el repo falla, vuelve a verse como no leida y se muestra el error
    //     (notificationsRepository.markAsRead(4) siempre falla, para probar esto)
    //   - marcar como leida no puede pisar cambios que hayan pasado mientras tanto
    // ---------------------------------------------------------------------------
    fun markAsRead(id: Int) {
    }

    // ---------------------------------------------------------------------------
    // TODO 7 - Notificaciones que llegan solas
    //
    //  notificationsRepository.liveNotifications() emite una notificacion nueva cada
    //  5 segundos y nunca termina.
    //
    //   - cada una que llega aparece arriba de todo en la lista, sin que el usuario
    //     haga nada
    //   - se sigue escuchando mientras el ViewModel viva
    // ---------------------------------------------------------------------------
    private fun observeLiveNotifications() {
    }
}
