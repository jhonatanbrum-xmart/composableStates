package com.codelabs.state.dashboard.solucion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.state.dashboard.DashboardUiState
import com.codelabs.state.dashboard.Notification
import com.codelabs.state.dashboard.NotificationFilter
import com.codelabs.state.dashboard.User
import com.codelabs.state.dashboard.Weather
import com.codelabs.state.dashboard.data.NotificationsRepository
import com.codelabs.state.dashboard.data.StatsRepository
import com.codelabs.state.dashboard.data.UserRepository
import com.codelabs.state.dashboard.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * SOLUCION DE REFERENCIA del ejercicio de ../EJERCICIO.md.
 *
 * No mirar antes de intentarlo. Esta clase no la usa nadie: la pantalla sigue
 * apuntando a DashboardViewModel, que es el que tiene los TODO.
 *
 * No es LA solucion, es UNA. Varios puntos tienen mas de una salida valida.
 */
class DashboardViewModelSolucion : ViewModel() {

    private val userRepository = UserRepository()
    private val weatherRepository = WeatherRepository()
    private val notificationsRepository = NotificationsRepository()
    private val statsRepository = StatsRepository()

    private val _user = MutableStateFlow<User?>(null)
    private val _weather = MutableStateFlow<Weather?>(null)
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    private val _filter = MutableStateFlow(NotificationFilter.All)
    private val _engagementScore = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    // --- TODO 1: estado unico -------------------------------------------------
    //
    // Son 7 flows y combine llega hasta 5, asi que se combinan de a grupos: combine
    // devuelve un Flow, y un Flow puede entrar en otro combine. Notificaciones y
    // filtro van juntos porque el filtrado necesita los dos al mismo tiempo.
    //
    // unreadCount se calcula sobre la lista SIN filtrar: por eso sale de este mismo
    // bloque y no de la lista ya filtrada.

    private val visibleNotifications = combine(_notifications, _filter) { list, filter ->
        val visible = when (filter) {
            NotificationFilter.All -> list
            NotificationFilter.Unread -> list.filterNot { it.isRead }
            NotificationFilter.Read -> list.filter { it.isRead }
        }
        Triple(visible, filter, list.count { !it.isRead })
    }

    private val loadStatus = combine(_isLoading, _error) { loading, error -> loading to error }

    // WhileSubscribed(5_000): deja de recalcular cuando nadie mira, pero aguanta 5s
    // para que una rotacion de pantalla no tire y rearme todo.
    val uiState: StateFlow<DashboardUiState> = combine(
        _user, _weather, visibleNotifications, _engagementScore, loadStatus,
    ) { user, weather, (visible, filter, unread), score, (loading, error) ->
        DashboardUiState(
            isLoading = loading,
            user = user,
            weather = weather,
            notifications = visible,
            filter = filter,
            unreadCount = unread,
            engagementScore = score,
            error = error,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    // --- TODO 2: carga inicial ------------------------------------------------
    init {
        refresh()
        observeLiveNotifications()
        // El score se recalcula solo cada vez que cambia la lista, venga el cambio
        // de donde venga (refresh, markAsRead o una notificacion live).
        _notifications.onEach { recalculateScore(it) }.launchIn(viewModelScope)
    }

    // --- TODO 3: refresh ------------------------------------------------------
    //
    // async x3 = las tres llamadas arrancan juntas: 1.5s en vez de 3.5s.
    //
    // El runCatching va ADENTRO de cada async, no afuera del launch. Si un async
    // dejara escapar la excepcion, se llevaria puestos a los hermanos: son hijos
    // del mismo launch, y el launch es un Job comun. Que viewModelScope tenga
    // SupervisorJob no ayuda, porque la supervision solo alcanza a sus hijos
    // directos (el launch), no a los nietos.

    private var refreshJob: Job? = null

    fun refresh() {
        // Cancelar el refresh anterior: dos toques seguidos no se pisan entre si.
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val user = async { runCatching { userRepository.getUser() } }
            val weather = async { runCatching { weatherRepository.getWeather("Montevideo") } }
            val notifications = async { runCatching { notificationsRepository.getNotifications() } }

            user.await().onSuccess { _user.value = it }.onFailure { _error.value = it.message }
            weather.await().onSuccess { _weather.value = it }.onFailure { _error.value = it.message }
            notifications.await()
                .onSuccess { fresh -> _notifications.update { fresh } }
                .onFailure { _error.value = it.message }

            // Se apaga el loading aunque haya fallado algo, y lo que si llego quedo puesto.
            _isLoading.value = false
        }
    }

    // --- TODO 4: score --------------------------------------------------------
    //
    // withContext(Dispatchers.Default) saca el calculo del hilo principal: la lista
    // sigue scrolleando.
    //
    // Ojo con una sutileza: computeEngagementScore() es un loop que no suspende, asi
    // que cancelar el job NO lo frena en el aire; lo que hace el cancel es descartar
    // el resultado viejo para que no pise al nuevo. Alcanza para que gane el ultimo.
    // (Otra salida al mismo problema: _notifications.mapLatest { ... }.)

    private var scoreJob: Job? = null

    private fun recalculateScore(notifications: List<Notification>) {
        scoreJob?.cancel()
        scoreJob = viewModelScope.launch {
            val score = withContext(Dispatchers.Default) {
                statsRepository.computeEngagementScore(notifications)
            }
            _engagementScore.value = score
        }
    }

    // --- TODO 5: filtro -------------------------------------------------------
    // Sincronico, sin corrutina: el filtrado ya vive adentro del combine.
    fun onFilterChanged(filter: NotificationFilter) {
        _filter.value = filter
    }

    // --- TODO 6: marcar como leida (optimista + rollback) ---------------------
    //
    // update {} y no .value = : mientras corren los 300ms del repo puede llegar una
    // notificacion live y agregarse a la lista. Con .value = se perderia; con update
    // se aplica sobre lo que haya en ese momento.

    fun markAsRead(id: Int) {
        val current = _notifications.value.firstOrNull { it.id == id } ?: return
        if (current.isRead) return

        // Optimista: la UI cambia ya, sin esperar al repo.
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }

        viewModelScope.launch {
            runCatching { notificationsRepository.markAsRead(id) }.onFailure { error ->
                _notifications.update { list ->
                    list.map { if (it.id == id) it.copy(isRead = false) else it }
                }
                _error.value = error.message
            }
        }
    }

    // --- TODO 7: notificaciones live ------------------------------------------
    //
    // launchIn(viewModelScope) = el collect vive mientras viva el ViewModel y se
    // cancela solo cuando muere. No hay nada que limpiar a mano.

    private fun observeLiveNotifications() {
        notificationsRepository.liveNotifications()
            .onEach { new -> _notifications.update { listOf(new) + it } }
            .launchIn(viewModelScope)
    }
}
