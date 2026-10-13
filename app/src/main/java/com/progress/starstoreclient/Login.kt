package com.progress.starstoreclient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.progress.starstoreclient.ui.theme.droidBesh
import com.progress.starstoreclient.ui.theme.starJedi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun Login() = Dialog({}) {
    val api: StarStoreApi by lazy { StarStoreApi.getApi() }
    var edit by remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }

    val color = if (error)
        MaterialTheme.colorScheme.error
    else
        MaterialTheme.colorScheme.primary

    val style = TextStyle(
        color,
        16.sp,
        fontFamily = if (name == "") starJedi else droidBesh
    )

    val scope = rememberCoroutineScope()

    OutlinedCard(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color),
        colors = CardDefaults.outlinedCardColors(contentColor = color)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (name == "") "name: " else "pass: ",
                fontFamily = starJedi
            )
            BasicTextField(
                value = edit,
                onValueChange = { edit = it },
                singleLine = true,
                textStyle = style
            )
            IconButton({
                if (name == "") {
                    name = edit
                    edit = ""
                }
                else scope.launch {
                    try {
                        api.login(name, edit)
                        error = false
                        StarStoreApi.user = name
                        StarStoreApi.pass = edit
                    } catch (e: HttpException) {
                        error = true
                        errorMessage = when (e.code()) {
                            401, 403 -> "Incorrect username or password"
                            else -> "Server error: ${e.code()}"
                        }
                        name = ""
                        edit = ""
                    } catch (e: IOException) {
                        error = true
                        errorMessage = "Network error. Please check your connection"
                        name = ""
                        edit = ""
                    } catch (e: IOException) {
                        error = true
                        errorMessage = e.localizedMessage ?: "An unknown error occurred"
                        name = ""
                        edit = ""
                    }
                }
            }) {
                Icon(Icons.AutoMirrored.Default.ArrowForward, null)
            }
        }

        if (error)
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 6.dp),
                textAlign = TextAlign.Center
            )
    }
}
