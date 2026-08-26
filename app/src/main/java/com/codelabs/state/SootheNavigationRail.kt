package com.codelabs.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun SootheNavigationRail(
    isProfileButtonClicked: Boolean,
    onButtonClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
            modifier = modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
            NavigationRailItem(icon = {
                Icon(
                    imageVector = Icons.Default.DateRange, contentDescription = null
                )
            }, label = {
                Text(stringResource(R.string.works_button))
            }, selected = !isProfileButtonClicked, onClick = {
                onButtonClicked(false)
            })
            NavigationRailItem(icon = {
                Icon(
                    imageVector = Icons.Default.AddCircle, contentDescription = null
                )
            }, label = {
                Text(stringResource(R.string.water_button))
            }, selected = isProfileButtonClicked, onClick = {
                onButtonClicked(true)
            })
        }
    }
}