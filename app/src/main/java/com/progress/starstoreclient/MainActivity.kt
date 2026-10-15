package com.progress.starstoreclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.progress.starstoreclient.ui.theme.StarStoreClientTheme
import androidx.core.content.edit
import androidx.compose.animation.AnimatedVisibility

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val pref = getPreferences(MODE_PRIVATE)

        val prefUser = "user"
        val prefPass = "pass"

        StarStoreApi.user = pref.getString(prefUser, "") ?: ""
        StarStoreApi.pass = pref.getString(prefPass, "") ?: ""

        fun save() = pref.edit {
            putString(prefUser, StarStoreApi.user)
            putString(prefPass, StarStoreApi.pass)
        }

        setContent {
            var id by rememberSaveable { mutableLongStateOf(-1L) }
            var tab by rememberSaveable { mutableIntStateOf(3) }

            BackHandler(id >= 0) { id = -1 }

            StarStoreClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Tabs(tab) {
                            tab = it
                            id = -1L
                        }

                        AnimatedVisibility(
                            id < 0,
                            modifier = Modifier.weight(1f),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Main(tab) { id = it }
                        }
                        AnimatedVisibility(
                            id >= 0,
                            modifier = Modifier.weight(1f),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Detail(id)
                        }

                        AnimatedVisibility(
                            StarStoreApi.user == "",
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Login() {
                                save()
                            }
                        }
                        AnimatedVisibility(
                            StarStoreApi.user != "",
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }
                        ) {
                            User {
                                StarStoreApi.user = ""
                                save()
                                id = -1L
                            }
                        }
                    }
                }
            }
        }
    }
}