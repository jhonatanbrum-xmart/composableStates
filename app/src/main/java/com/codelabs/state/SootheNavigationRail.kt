package com.codelabs.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

enum class AppTab(val labelRes: Int, val icon: ImageVector) {
    Tasks(R.string.works_button, Icons.Default.DateRange),
    Water(R.string.water_button, Icons.Default.AddCircle),
    Dashboard(R.string.dashboard_button, Icons.Default.Home),
}

@Composable
fun SootheNavigationRail(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(), containerColor = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            AppTab.entries.forEach { tab ->
                NavigationRailItem(icon = {
                    Icon(
                        imageVector = tab.icon, contentDescription = null
                    )
                }, label = {
                    Text(stringResource(tab.labelRes))
                }, selected = selectedTab == tab, onClick = {
                    onTabSelected(tab)
                })
            }
        }
    }
}
