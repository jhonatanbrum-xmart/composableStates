package com.codelabs.state.wellenes.wellenesTask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun WellnessTaskItem(
    task: WellnessTask,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    openTheKeyBoard: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        LaunchedEffect(openTheKeyBoard) {
            if (openTheKeyBoard) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f)
                .padding(start = 16.dp),
            value = task.label,
            placeholder = {
                Text("Task # ${task.id}")
            },
            onValueChange = onNameChange,
            singleLine = true
        )
        Checkbox(
            checked = task.isChecked, onCheckedChange = onCheckedChange
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "")
        }
    }
}

