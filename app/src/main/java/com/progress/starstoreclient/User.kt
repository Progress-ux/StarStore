package com.progress.starstoreclient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.progress.starstoreclient.ui.theme.droidBesh
import retrofit2.HttpException
import java.text.DecimalFormat

@Composable
fun User(
    onLogout: () -> Unit
) = Surface(
    color = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.primary
) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }

    var menu by remember { mutableStateOf(false) }

    val money = remember { DecimalFormat("###,###,###,##0.##ᖬ") }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    val credits by produceState(0L) {
        runCatching {
            api.money()
        }.onSuccess { money ->
            value = money
            errorText = null
        }.onFailure { exception ->
            errorText = when (exception) {
                is HttpException -> when (exception.code()) {
                    401, 403 -> "Нет доступа"
                    else -> "Ошибка сервера: ${exception.code()}"
                }
                is java.io.IOException -> "Нет соединения"
                else -> "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
            }
        }
    }

    Box() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = if (errorText != null) errorText!!
                       else money.format(credits),
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable {
                    menu = true
                }
                .padding(8.dp)
                .align(Alignment.Center)
        ) {
            AsyncImage(
                StarStoreApi.IMAGES + StarStoreApi.user + ".png",
                null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(42.dp)
            )

            DropdownMenu(menu, { menu = false } ) {
                IconButton(onLogout) {
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, "Exit")
                }
            }
        }
    }
}