package com.progress.starstoreclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.progress.starstoreclient.ui.theme.StarStoreClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var id by rememberSaveable { mutableLongStateOf(-1L) }

            BackHandler(id >= 0) { id = -1 }

            StarStoreClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedVisibility(
                        id < 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Main(
                            modifier = Modifier.padding(innerPadding),
                            click = {
                                id = it
                            }
                        )
                    }
                    AnimatedVisibility(
                        id >= 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Detail(
                            modifier = Modifier
                                .padding(innerPadding),
                            id = id
                        )
                    }
                }
            }
        }
    }
}