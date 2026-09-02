package com.codelabs.state.counter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun StatefulScreen(
    modifier: Modifier = Modifier, viewModel: CounterViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val counter by viewModel.counter.collectAsStateWithLifecycle()
    val runningIcons by viewModel.runningIcons.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        runningIcons.forEach { iconData ->
            key(iconData.id) {
                AnimateFloatingIcon(
                    iconData = iconData,
                    onAnimationEnd = { viewModel.removeFloatingIcon(iconData) }
                )
            }
        }
        StatelessCounter(
            count = counter,
            onIncrement = { viewModel.onIncrement(screenWidthPx, screenHeightPx) },
            modifier = modifier.align(Alignment.Center)
        )
    }
}