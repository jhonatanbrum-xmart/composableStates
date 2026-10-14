package com.codelabs.state.catalog.domain.model

// Capa de dominio: Kotlin puro. Nada de Android, Compose ni R acá adentro.
// (Los textos e íconos de cada categoría viven en la capa de UI: ui/CategoryUi.kt)

enum class Category { Mindfulness, Cardio, Strength, Outdoor }

enum class Intensity { Low, Medium, High }

data class WellnessActivity(
    val id: Int,
    val name: String,
    val category: Category,
    val durationMinutes: Int,
    val intensity: Intensity,
    val description: String,
)

data class ActivitiesPage(
    val items: List<WellnessActivity>,
    val hasMore: Boolean,
)
