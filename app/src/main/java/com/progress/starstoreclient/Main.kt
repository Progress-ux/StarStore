package com.progress.starstoreclient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp

@Composable
fun Main(modifier: Modifier = Modifier) {
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

    when {
        models.isEmpty() && errorText.isNullOrEmpty() -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        models.isEmpty() && errorText != null -> {
            Box(modifier = modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.Center) {
                Text(text = "Ошибка: $errorText", color = androidx.compose.ui.graphics.Color.Red)
            }
        }
        else -> {
            LazyColumn(modifier = modifier) {
                items(models) { model ->
                    Row(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(model.name, modifier = Modifier.weight(1f))
                        Text(model.price.toString())
                    }
                }
            }

        }
    }
}