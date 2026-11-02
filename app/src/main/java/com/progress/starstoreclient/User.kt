package com.progress.starstoreclient

import android.R.attr.name
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.DecimalFormat

@Composable
fun User(
    onLogout: () -> Unit,
    afterBuy: () -> Unit
) = Surface(
    color = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.primary
) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }
    val scope = rememberCoroutineScope()

    var menu by remember { mutableStateOf(false) }
    var buy by remember { mutableStateOf(false) }

    val money = remember { DecimalFormat("###,###,###,##0.##ᖬ") }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    val credits by produceState(0L, StarStoreApi.changes) {
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
                is IOException -> "Нет соединения"
                else -> "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
            }
        }
    }

    val cost by produceState(0L, StarStoreApi.changes) {
        runCatching {
            api.cost()
        }.onSuccess {
            value = it
            errorText = null
        }.onFailure { exception ->
            errorText = when (exception) {
                is HttpException -> when (exception.code()) {
                    401, 403 -> "Нет доступа"
                    else -> "Ошибка сервера: ${exception.code()}"
                }
                is IOException -> "Нет соединения"
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
                .clickable(cost > 0) { buy = true }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (errorText != null) errorText!!
                       else money.format(credits),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = money.format(cost),
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
        if (buy) Dialog({
            buy = false
        }) {
            Buy {
                buy = false
                scope.launch {
                    try {
                        api.buy()
                        StarStoreApi.changes++
                        afterBuy()
                    } catch (e: HttpException) {
                        errorText = when (e.code()) {
                            401, 403 -> "Incorrect username or password"
                            else -> "Server error: ${e.code()}"
                        }
                    } catch (e: IOException) {
                        errorText = e.localizedMessage ?: "An unknown error occurred"
                    }
                }
            }
        }
    }
}