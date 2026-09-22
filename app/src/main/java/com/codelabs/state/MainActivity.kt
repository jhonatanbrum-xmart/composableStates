package com.codelabs.state

import android.os.Bundle
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
import com.codelabs.state.dashboard.DashboardScreen
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
                }
            }
        }
    }
}

@Composable
fun ThemeWindows() {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Tasks) }
    Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        SootheNavigationRail(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.background(color = Color(0xFF694832))
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                AppTab.Tasks -> WellnessScreen()
                AppTab.Water -> StatefulScreen()
                AppTab.Dashboard -> DashboardScreen()
            }
        }
    }
}


@Preview
@Composable
private fun Themewindowspreview() {
    ThemeWindows()
}
