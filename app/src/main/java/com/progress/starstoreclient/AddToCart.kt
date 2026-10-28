package com.progress.starstoreclient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.progress.starstoreclient.ui.theme.starJedi

@Composable
fun AddToCart(value: Long, add: (Long) -> Unit) = OutlinedCard(
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
) {
    var amount by rememberSaveable {
        mutableLongStateOf(if (value > 0L) value else 1L)
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(18.dp)
    ) {
        Text(
            text = amount.toString(),
            fontFamily = starJedi,
            modifier = Modifier
                .padding(start = 12.dp, end = 22.dp)
        )

        Column(verticalArrangement = Arrangement.Center) {
            IconButton(
                { amount += 1 },
                Modifier
                    .padding(bottom = 6.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Add, null)
            }
            IconButton(
                {
                    if (amount > 0L) amount -= 1
                },
                Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Remove, null)
            }
        }

        IconButton(
            onClick = { add(amount) },
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                null,
            )
        }
    }
}