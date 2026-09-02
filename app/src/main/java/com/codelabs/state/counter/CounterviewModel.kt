package com.codelabs.state.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random


class CounterViewModel : ViewModel() {

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _runningIcons = MutableStateFlow<List<FloatingIcon>>(emptyList())
    val runningIcons: StateFlow<List<FloatingIcon>> = _runningIcons.asStateFlow()

    fun onIncrement(screenWidthPx: Float, screenHeightPx: Float) {
        _counter.value++
        addFloatingIcon(screenWidthPx, screenHeightPx)
    }

    private fun addFloatingIcon(screenWidthPx: Float, screenHeightPx: Float) {
        val randomX = Random.nextFloat() * (screenWidthPx - 41f)
        val destinationY = -150f
        _runningIcons.update { current ->
            current + FloatingIcon(
                initialX = randomX,
                initialY = screenHeightPx,
                targetY = destinationY,
                imageIndex = Random.nextInt(1, 6)
            )
        }
    }

    fun removeFloatingIcon(icon: FloatingIcon) {
        _runningIcons.update { current -> current.filter { it.id != icon.id } }
    }
}