package com.codelabs.state.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.R

/**
 * La UI del ejercicio ya esta hecha: no hay nada para tocar aca.
 * Todo lo que falta vive en [DashboardViewModel].
 */
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onFilterChanged = viewModel::onFilterChanged,
        onNotificationClicked = viewModel::markAsRead,
        modifier = modifier,
    )
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onRefresh: () -> Unit,
    onFilterChanged: (NotificationFilter) -> Unit,
    onNotificationClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = uiState.user?.let { stringResource(R.string.dashboard_greeting, it.name) }
                    ?: stringResource(R.string.dashboard_button),
                style = MaterialTheme.typography.headlineSmall,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.dashboard_refresh),
                    )
                }
            }
        }

        uiState.error?.let { message ->
            ErrorBanner(message = message, modifier = Modifier.padding(top = 8.dp))
        }

        uiState.user?.let { user ->
            InfoCard(
                title = user.email,
                subtitle = stringResource(R.string.dashboard_streak, user.streakDays),
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        uiState.weather?.let { weather ->
            InfoCard(
                title = weather.city,
                subtitle = stringResource(
                    R.string.dashboard_weather,
                    weather.temperature,
                    weather.description,
                ),
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Text(
            text = uiState.engagementScore
                ?.let { stringResource(R.string.dashboard_score, it) }
                ?: stringResource(R.string.dashboard_score_loading),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.dashboard_notifications),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.dashboard_unread, uiState.unreadCount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NotificationFilter.entries.forEach { filter ->
                FilterChip(
                    selected = uiState.filter == filter,
                    onClick = { onFilterChanged(filter) },
                    label = { Text(stringResource(filter.labelRes)) },
                )
            }
        }

        if (uiState.notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.dashboard_empty),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = uiState.notifications, key = { it.id }) { notification ->
                    NotificationRow(
                        notification = notification,
                        onClick = { onNotificationClicked(notification.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null)
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun NotificationRow(notification: Notification, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = if (notification.isRead) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.Notifications
                },
                contentDescription = null,
                tint = if (notification.isRead) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (notification.isRead) TextDecoration.LineThrough else null,
            )
        }
    }
}

private val NotificationFilter.labelRes: Int
    get() = when (this) {
        NotificationFilter.All -> R.string.dashboard_filter_all
        NotificationFilter.Unread -> R.string.dashboard_filter_unread
        NotificationFilter.Read -> R.string.dashboard_filter_read
    }
