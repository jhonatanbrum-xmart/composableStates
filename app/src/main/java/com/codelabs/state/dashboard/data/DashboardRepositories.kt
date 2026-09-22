package com.codelabs.state.dashboard.data

import com.codelabs.state.dashboard.Notification
import com.codelabs.state.dashboard.User
import com.codelabs.state.dashboard.Weather
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repos fake del ejercicio. Data hardcodeada + delay para simular red.
 *
 * Ojo: ninguno de estos metodos cambia de hilo por su cuenta. Eso es a proposito:
 * decidir en que dispatcher corre cada cosa es parte del ejercicio, y se resuelve
 * en el ViewModel.
 */

class UserRepository {

    suspend fun getUser(): User {
        delay(800)
        return User(
            name = "Joaco",
            email = "joaquin.vidal@xmartlabs.com",
            streakDays = 7,
        )
    }
}

class WeatherRepository {

    /** Poner en true para probar el camino de error sin tocar codigo. */
    var shouldFail: Boolean = false

    suspend fun getWeather(city: String): Weather {
        delay(1200)
        if (shouldFail) error("No se pudo obtener el clima de $city")
        return Weather(city = city, temperature = 21, description = "Parcialmente nublado")
    }
}

class NotificationsRepository {

    suspend fun getNotifications(): List<Notification> {
        delay(1500)
        return listOf(
            Notification(id = 1, title = "Tu reporte semanal ya esta listo"),
            Notification(id = 2, title = "Nico comento en tu tarea", isRead = true),
            Notification(id = 3, title = "Recorda tomar agua"),
            Notification(id = 4, title = "3 tareas vencen manana"),
            Notification(id = 5, title = "Se actualizo la politica de vacaciones", isRead = true),
            Notification(id = 6, title = "Tenes una reunion en 30 minutos"),
        )
    }

    suspend fun markAsRead(id: Int) {
        delay(300)
        if (id == FAILING_ID) error("No se pudo marcar como leida la notificacion $id")
    }

    /**
     * Flujo infinito: cada 5 segundos llega una notificacion nueva del server.
     * Nunca termina por si solo.
     */
    fun liveNotifications(): Flow<Notification> = flow {
        var nextId = 100
        while (true) {
            delay(5_000)
            emit(Notification(id = nextId, title = "Notificacion en vivo #${nextId - 99}"))
            nextId++
        }
    }

    companion object {
        /** Esta notificacion siempre falla al marcarse como leida (para probar el rollback). */
        const val FAILING_ID = 4
    }
}

class StatsRepository {

    /**
     * OJO: no es suspend y bloquea el hilo desde el que se la llame por varios cientos
     * de ms. Si se la invoca en el hilo principal, la UI se traba y se nota.
     */
    fun computeEngagementScore(notifications: List<Notification>): Int {
        var acc = 0L
        repeat(60_000_000) { i ->
            acc = (acc + i * 31L) % 1_000_003L
        }
        val read = notifications.count { it.isRead }
        return if (notifications.isEmpty()) 0 else (read * 100 / notifications.size) + (acc % 2).toInt()
    }
}
