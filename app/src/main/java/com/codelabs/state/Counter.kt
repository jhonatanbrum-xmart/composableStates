package com.codelabs.state

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (count > 0) {
            Text(stringResource(R.string.counter_text, count))
        }
        Button(
            onClick = onIncrement, modifier = modifier.padding(12.dp)
        ) {
            Text("Add one")
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun StatefulCounter(modifier: Modifier = Modifier) {
    var counter by rememberSaveable { mutableStateOf(0) }
    val runningIcons = remember { mutableStateListOf<FloatingIcon>() }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    Box(modifier = Modifier.fillMaxSize()) {
        runningIcons.forEach { iconData ->
            key(iconData.id) {
                AnimateFloatingIcon(
                    iconData = iconData, onAnimationEnd = { runningIcons.remove(iconData) })
            }
        }
        StatelessCounter(
            count = counter, onIncrement = {
                counter++
                val randomX = Random.nextFloat() * (screenWidthPx - 410f)
                val spawnY = screenHeightPx + 100f
                val destinationY = -150f
                runningIcons.add(
                    FloatingIcon(
                        initialX = randomX,
                        initialY = spawnY,
                        targetY = destinationY,
                        imageIndex = Random.nextInt(1, 6)
                    )
                )
            }, modifier = modifier.align(Alignment.Center)
        )
    }
}

