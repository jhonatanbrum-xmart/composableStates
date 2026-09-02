package com.codelabs.state.wellenes

import androidx.annotation.StringRes
import com.codelabs.state.R

enum class StateOfWellness(@StringRes val label: Int) {
    All(R.string.AllTasksFilter_text), CheckedTasks(R.string.CheckedTasksFilter_text), NoCheckedTasks(R.string.NoCheckedTasksFilter_text)
}