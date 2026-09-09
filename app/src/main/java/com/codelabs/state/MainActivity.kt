package com.codelabs.state

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.codelabs.state.counter.StatefulScreen
import com.codelabs.state.ui.theme.BasicStateCodelabTheme
import com.codelabs.state.wellenes.WellnessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicStateCodelabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ThemeWindows()
                    try {
                    } catch (e: Exception) {
                        Log.d("tag", "$e")
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeWindows() {
    var isWaterGlassesButtonClicked by rememberSaveable { mutableStateOf(false) }
    Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        SootheNavigationRail(
            IsWaterGlassesButtonClicked = true,
            onButtonClicked = { isWaterGlassesButtonClicked = it },
            modifier = Modifier.background(color = Color(0xFF694832))
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isWaterGlassesButtonClicked) {
                StatefulScreen()
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