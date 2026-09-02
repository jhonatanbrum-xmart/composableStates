package com.codelabs.state.wellenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.ui.theme.BasicStateCodelabTheme
import com.codelabs.state.wellenes.wellenesTask.WellnessTasksList

@Composable
fun WellnessScreen(
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    val tasks by wellnessViewModel.filteredTasks.collectAsStateWithLifecycle()
    val currentFilter by wellnessViewModel.filter.collectAsStateWithLifecycle()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StateOfWellness.entries.forEach { filterOption ->
                val selected = currentFilter == filterOption
                FilterChip(
                    selected = selected,
                    onClick = { wellnessViewModel.setFilter(filterOption) },
                    label = {
                        Text(stringResource(filterOption.label))
                    },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null)
            }
        }

        WellnessTasksList(
            list = tasks,
            onCheckedTask = { task, _ -> wellnessViewModel.changeTaskChecked(task) },
            onCloseTask = { task -> wellnessViewModel.removeTask(task) },
            onNameChange = { task, newName ->
                wellnessViewModel.onTaskNameChanged(taskId = task.id, newName = newName)
            })
    }
}

@Preview
@Composable
fun MyWellnessScreenPreview() {
    BasicStateCodelabTheme {
        WellnessScreen()
    }
}
