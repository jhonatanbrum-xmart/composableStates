package com.codelabs.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.ui.theme.BasicStateCodelabTheme

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier, wellnessViewModel: WellnessViewModel = viewModel()
) {
    FABAdding { wellnessViewModel.addTask() }
    val tasks by wellnessViewModel.tasks.collectAsStateWithLifecycle()
    WellnessTasksList(list = tasks, onCheckedTask = { task, checked ->
        wellnessViewModel.changeTaskChecked(task)
    }, onCloseTask = { task ->
        wellnessViewModel.removeTask(task)
    }, onNameChange = {task, newName -> wellnessViewModel.onTaskNameChanged(taskId = task.id, newName = newName)})

}

@Composable
fun FABAdding(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}


@Preview
@Composable
fun MyAppPreview() {
    BasicStateCodelabTheme {
        WellnessScreen()
    }
}
