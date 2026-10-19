package com.progress.starstoreclient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.progress.starstoreclient.ui.theme.starJedi

@Composable
fun FavButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(Modifier
        .padding(8.dp)
        .border(1.dp, MaterialTheme.colorScheme.primary)
        .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.padding(horizontal = 8.dp))
        Text(
            text,
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp),
            Color.Black,
            fontSize = 22.sp,
            fontFamily = starJedi
        )
    }
}