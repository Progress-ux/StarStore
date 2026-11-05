package com.progress.starstoreclient

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember

@Composable
fun DetailPager(
    id: Long,
    tab: Int,
    content: @Composable (Long) -> Unit
) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }

    val models by produceState(emptyList<StarStoreApi.Model>()) {
        runCatching {
            api.models()
        }.onSuccess { list ->
            value = list
        }
    }

    val faves by produceState(emptyList<Long>(), StarStoreApi.user) {
        runCatching {
            api.faves()
        }.onSuccess { list ->
            value = list
        }
    }

    val list by remember(tab) {
        derivedStateOf {
            models.filter {
                it.ship == tab.toLong() || tab == 3 || tab == 2 && it.id in faves
            }
        }
    }

    val state = rememberPagerState { list.size }

    LaunchedEffect(list) {
        state.scrollToPage(list.indexOfFirst { it.id == id })
    }

    HorizontalPager(state) {
        content(list[it].id)
    }
}