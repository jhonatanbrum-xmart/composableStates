package com.codelabs.state.wellenes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.state.wellenes.wellenesTask.WellnessTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class WellnessViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(getWellnessTasks())

    private val _filter = MutableStateFlow(StateOfWellness.all)
    val filter: StateFlow<StateOfWellness> = _filter.asStateFlow()

    val filteredTasks: StateFlow<List<WellnessTask>> = combine(_tasks, _filter) { tasks, filter ->
        when (filter) {
            StateOfWellness.all -> tasks
            StateOfWellness.checkedTasks -> tasks.filter { it.isChecked }
            StateOfWellness.noCheckedTasks -> tasks.filter { !(it.isChecked) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _tasks.value
    )

    fun setFilter(newFilter: StateOfWellness) {
        _filter.value = newFilter
    }

    fun removeTask(item: WellnessTask) {
        _tasks.update { currentList -> currentList.filter { it.id != item.id } }
    }

    fun addTask() {
        try {
            val newTaskId: Int = (_tasks.value.lastOrNull()?.id ?: 0) + 1
            val newTask = WellnessTask(id = newTaskId, label = "Task # $newTaskId")
            _tasks.value += newTask
        } catch (e: Exception) {
            Log.d("tag", "$e")
        }
    }

    fun changeTaskChecked(item: WellnessTask) {
        _tasks.update { currentList ->
            currentList.map { task ->
                if (task.id == item.id) task.copy(isChecked = !task.isChecked, categoryTask = true)
                else task
            }
        }
    }

    fun onTaskNameChanged(taskId: Int, newName: String) {
        _tasks.update { currentList ->
            currentList.map { task ->
                if (task.id == taskId) task.copy(label = newName) else task
            }
        }
    }
}


private fun getWellnessTasks() =
    List(20) { identifier -> WellnessTask(id = identifier, label = "Task # $identifier") }
