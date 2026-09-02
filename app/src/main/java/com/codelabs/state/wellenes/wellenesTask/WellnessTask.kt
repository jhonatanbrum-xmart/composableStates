package com.codelabs.state.wellenes.wellenesTask

data class WellnessTask(
    val id: Int,
    val label: String,
    val isChecked: Boolean = false,
    val categoryTask: Boolean = false
)