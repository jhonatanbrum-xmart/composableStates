package com.codelabs.state

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AnimateFloatingIcon(iconData: FloatingIcon, onAnimationEnd: () -> Unit ) {
    val animatableY = remember { Animatable(iconData.initialY) }
    LaunchedEffect(Unit) {
        animatableY.animateTo(
            targetValue = iconData.targetY, animationSpec = tween(
                durationMillis = 9000, easing = LinearEasing
            )
        )
        onAnimationEnd()
    }
    Image(
        painter = painterResource(when (iconData.imageIndex) {
            1 ->  R.drawable.vaso_de_agua
            2 -> R.drawable.agua_fria
            3 -> R.drawable.agua__3_
            4 -> R.drawable.agua__2_
            5 -> R.drawable.agua
            else -> R.drawable.vaso_de_agua
        }),
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .offset {
                IntOffset(
                    iconData.initialX.roundToInt(), animatableY.value.roundToInt()
                )
            })
}
