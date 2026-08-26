package com.codelabs.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.ui.theme.BasicStateCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicStateCodelabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ThemeWindows()
                }
            }
        }
    }
}

@Composable
fun ThemeWindows(
) {

    var isProfileButtonClicked by rememberSaveable { mutableStateOf(false) }
    Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        SootheNavigationRail(
            isProfileButtonClicked = isProfileButtonClicked,
            onButtonClicked = { isProfileButtonClicked = it },
            modifier = Modifier.background(color = Color(0xFF694832))

        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isProfileButtonClicked) {
                StatefulCounter()
            } else {
                WellnessScreen()
            }

        }
    }
}


@Preview
@Composable
private fun Themewindowspreview() {
    ThemeWindows()
}