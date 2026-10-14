package com.codelabs.state.catalog.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.codelabs.state.R
import com.codelabs.state.catalog.domain.model.Category
import com.codelabs.state.catalog.domain.model.Intensity

// Cómo se MUESTRA cada modelo de dominio. Vive en la capa de UI porque usa R y Compose,
// que el dominio no puede conocer. Ya está hecho.

val Category.labelRes: Int
    get() = when (this) {
        Category.Mindfulness -> R.string.catalog_category_mindfulness
        Category.Cardio -> R.string.catalog_category_cardio
        Category.Strength -> R.string.catalog_category_strength
        Category.Outdoor -> R.string.catalog_category_outdoor
    }

val Category.icon: ImageVector
    get() = when (this) {
        Category.Mindfulness -> Icons.Default.Face
        Category.Cardio -> Icons.Default.Star
        Category.Strength -> Icons.Default.Build
        Category.Outdoor -> Icons.Default.Place
    }

val Intensity.labelRes: Int
    get() = when (this) {
        Intensity.Low -> R.string.catalog_intensity_low
        Intensity.Medium -> R.string.catalog_intensity_medium
        Intensity.High -> R.string.catalog_intensity_high
    }
