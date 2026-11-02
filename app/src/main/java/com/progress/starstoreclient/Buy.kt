package com.progress.starstoreclient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.progress.starstoreclient.ui.theme.droidBesh
import com.progress.starstoreclient.ui.theme.starJedi
import retrofit2.HttpException
import java.text.DecimalFormat

@Composable
fun Buy(buy: () -> Unit) = OutlinedCard(
    modifier = Modifier.padding(20.dp),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.outlinedCardColors(contentColor = MaterialTheme.colorScheme.primary),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
) {
    val money = remember { DecimalFormat("###,###,###,##0.##ᖬ") }

    val api: StarStoreApi by lazy { StarStoreApi.getApi() }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    val cartModels by produceState(emptyList<Long>(), StarStoreApi.user) {
        runCatching {
            api.cartModels()
        }.onSuccess { list ->
            value = list
            errorText = null
        }.onFailure { exception ->
            value = emptyList()
            errorText = "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
        }
    }

    val models by produceState(emptyList<StarStoreApi.Model>()) {
        runCatching {
            api.models()
        }.onSuccess { list ->
            value = list.filter { it.id in cartModels }
            errorText = null
        }.onFailure { exception ->
            errorText = "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
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
                is java.io.IOException -> "Нет соединения"
                else -> "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
            }
        }
    }

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


    Column(
        modifier = Modifier.padding(12.dp).fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(models) { model ->
                val amount by produceState(0L) {
                    runCatching {
                        api.cart(model.id)
                    }.onSuccess {
                        value = it
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

                Column {
                    Text(
                        text = model.name,
                        fontFamily = starJedi,
                    )
                    Row() {
                        Text(
                            text = "${money.format(model.price)} × $amount",
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = " = ${money.format(model.price * amount)}",
                            maxLines = 1,
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(top = 6.dp, bottom = 6.dp))
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = money.format(cost),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (credits >= cost)
                IconButton(
                    onClick = {
                        buy()
                    },
                ) {
                    Icon(Icons.Default.ShoppingCart, null)
                }
            else {
                Text(
                    text = "insufficient funds",
                    fontFamily = droidBesh
                )
            }
        }
    }
}