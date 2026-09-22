package com.codelabs.state.dashboard

data class User(
    val name: String,
    val email: String,
    val streakDays: Int,
)

data class Weather(
    val city: String,
    val temperature: Int,
    val description: String,
)

data class Notification(
    val id: Int,
    val title: String,
    val isRead: Boolean = false,
)

enum class NotificationFilter { All, Unread, Read }

data class DashboardUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val weather: Weather? = null,
    val notifications: List<Notification> = emptyList(),
    val filter: NotificationFilter = NotificationFilter.All,
    val unreadCount: Int = 0,
    val engagementScore: Int? = null,
    val error: String? = null,
)
