package com.codelabs.state.wellenes.wellenesTask

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WellnessTasksList(
    list: List<WellnessTask>,
    onNameChange: (WellnessTask, String) -> Unit,
    onCheckedTask: (WellnessTask, Boolean) -> Unit,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    lastCreatedTask: Int?
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
        list.forEach { task ->
            WellnessTaskItem(
                task = task,
                onNameChange = { newName -> onNameChange(task, newName) },
                onCheckedChange = { checked -> onCheckedTask(task, checked) },
                onClose = { onCloseTask(task) },
                openTheKeyBoard = task.id == lastCreatedTask
            )
        }
    }
}