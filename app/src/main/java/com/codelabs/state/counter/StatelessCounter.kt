package com.codelabs.state.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codelabs.state.R


@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (count > 0) {
            Text(stringResource(R.string.counter_text, count))
        }
        Button(
            onClick = onIncrement, modifier = modifier.padding(12.dp)
        ) {
            Text(stringResource(R.string.button_text))
        }
    }
}