package com.progress.starstoreclient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.progress.starstoreclient.ui.theme.starJedi

@Composable
fun Tabs(tab: Int, onClick: (Int) -> Unit) {
    PrimaryTabRow(selectedTabIndex = tab) {
        Tab(selected = tab == 0, onClick = { onClick(0) }) {
            Text("veh ⛟", fontFamily = starJedi)
        }
        Tab(selected = tab == 1, onClick = { onClick(1) }) {
            Text("ship ✈", fontFamily = starJedi)
        }
        Tab(selected = tab == 2, onClick = { onClick(2) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("fav ", fontFamily = starJedi)
                Icon(
                    Icons.Default.FavoriteBorder,
                    null,
                    Modifier.size(16.dp)
                )
            }
        }
        Tab(selected = tab == 3, onClick = { onClick(3) }) {
            Text("all ∞", fontFamily = starJedi)
        }
    }
}