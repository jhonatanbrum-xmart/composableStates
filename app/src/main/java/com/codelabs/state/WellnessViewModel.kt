package com.codelabs.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WellnessViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(getWellnessTasks())
    val tasks: StateFlow<List<WellnessTask>> = _tasks.asStateFlow()


    fun removeTask(item: WellnessTask) {
        _tasks.update { currentList ->
            currentList.filter { it.id != item.id }
        }
    }

    fun addTask(){
        val newTaskId: Int = _tasks.value.last().id +1
        val newTask: WellnessTask = WellnessTask(id = newTaskId, label = "Task # $newTaskId")
        _tasks.value += newTask
    }

    fun changeTaskChecked(item: WellnessTask) {
        _tasks.update { currentList ->
            currentList.map { task ->
                if (task.id == item.id) {
                    task.copy(isChecked = !task.isChecked)
                } else task
            }
        }
    }



    fun onTaskNameChanged(taskId: Int, newName: String) {
        _tasks.update { currentList ->
            currentList.map { task ->
                if (task.id == taskId) {
                    task.copy(label = newName)
                } else task
            }
        }
    }
}



private fun getWellnessTasks() =
    List(20) { identifier -> WellnessTask(id = identifier, label = "Task # $identifier") }
