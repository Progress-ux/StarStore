package com.progress.starstoreclient

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.progress.starstoreclient.ui.theme.droidBesh
import com.progress.starstoreclient.ui.theme.starJedi
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Main(
    modifier: Modifier = Modifier,
    click: (Long) -> Unit
) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }

    var errorText by remember { mutableStateOf<String?>(null) }

    val models by produceState(emptyList<StarStoreApi.Model>()) {
        runCatching {
            api.models()
        }.onSuccess { list ->
            value = list
            errorText = null
        }.onFailure { exception ->
            errorText = "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
        }
    }

    val money = remember { DecimalFormat("###,###,###,###.00ᖬ") }

    when {
        models.isEmpty() && errorText.isNullOrEmpty() -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        models.isEmpty() && errorText != null -> {
            Box(modifier = modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Ошибка: $errorText", color = androidx.compose.ui.graphics.Color.Red)
            }
        }
        else -> {
            LazyColumn(modifier = modifier) {
                itemsIndexed(models) { index, model ->
                    val lang by produceState(initialValue = 1f) {
                        delay((index * 500 + 500L).milliseconds)
                        value = 0f
                    }
                    val droid by animateFloatAsState(targetValue = lang, label = "droid")
                    val human by animateFloatAsState(targetValue = 1 - lang, label = "human")

                    Row(Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable {
                            click(model.id)
                        }
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // Droid model name
                            Text(
                                text = model.name,
                                fontFamily = droidBesh,
                                modifier = Modifier.scale(1f, droid),
                                maxLines = 1
                            )
                            // Human model name
                            Text(
                                text = model.name,
                                modifier = Modifier.scale(1f, human),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = money.format(model.price),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

        }
    }
}