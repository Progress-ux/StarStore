package com.progress.starstoreclient

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.progress.starstoreclient.ui.theme.droidBesh
import com.progress.starstoreclient.ui.theme.starJedi
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Detail(
    modifier: Modifier,
    id: Long
) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }
    val model by produceState(StarStoreApi.Model()) {
        runCatching {
            api.model(id)
        }.onSuccess { list ->
            value = list
            errorText = null
        }.onFailure { exception ->
            errorText = "${exception.javaClass.simpleName}: ${exception.localizedMessage}"
        }
    }

    val money = remember { DecimalFormat("###,###,###,###.00") }
    val lang by produceState(initialValue = 1f) {
        delay(2000.milliseconds)
        value = 0f
    }
    val droid by animateFloatAsState(targetValue = lang, label = "droid")
    val human by animateFloatAsState(targetValue = 1 - lang, label = "human")

    Column(modifier.padding(8.dp)) {
        // Model image
        AsyncImage(
            StarStoreApi.IMAGES + model.image,
            null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        )

        // Model name
        Box() {
            // droid
            Text(
                text = model.name,
                fontSize = 24.sp,
                modifier = Modifier.scale(1f, droid),
                fontWeight = FontWeight.SemiBold,
                fontFamily = droidBesh
            )
            // human
            Text(
                text = model.name,
                fontSize = 24.sp,
                modifier = Modifier.scale(1f, human),
                fontWeight = FontWeight.SemiBold,
                fontFamily = starJedi
            )

        }
        Row() {
            // Model price
            Text(
                text = "$",
                fontFamily = droidBesh,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = money.format(model.price),
                fontFamily = starJedi,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider()
        // Model info
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // droid
            Text(
                text = model.info ?: "no data",
                fontFamily = droidBesh,
                modifier = Modifier.scale(1f, droid),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify,
            )
            // human
            Text(
                text = model.info ?: "no data",
                fontFamily = starJedi,
                modifier = Modifier.scale(1f, human),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify,
            )
        }
    }
}