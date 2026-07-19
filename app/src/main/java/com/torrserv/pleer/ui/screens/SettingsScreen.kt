package com.torrserv.pleer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrserv.pleer.storage.PreferencesManager
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var torrservUrl by remember { mutableStateOf("") }
    var lampaUrl by remember { mutableStateOf("") }
    var bufferSize by remember { mutableStateOf("100") }

    LaunchedEffect(Unit) {
        scope.launch {
            torrservUrl = preferencesManager.getTorrservUrl()
            lampaUrl = preferencesManager.getLampaUrl()
            bufferSize = preferencesManager.getBufferSize().toString()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок
        Text(
            text = "⚙ Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Torrserv URL
        Text(
            text = "Torrserv Server URL",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextField(
            value = torrservUrl,
            onValueChange = { torrservUrl = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = { Text("http://localhost:8090") }
        )

        // Lampa URL
        Text(
            text = "Lampa Server URL",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextField(
            value = lampaUrl,
            onValueChange = { lampaUrl = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = { Text("http://localhost:8085") }
        )

        // Buffer Size
        Text(
            text = "Buffer Size (MB)",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextField(
            value = bufferSize,
            onValueChange = { bufferSize = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = { Text("100") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Кнопки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onBackPressed,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    scope.launch {
                        preferencesManager.saveTorrservUrl(torrservUrl)
                        preferencesManager.saveLampaUrl(lampaUrl)
                        preferencesManager.saveBufferSize(bufferSize.toFloatOrNull() ?: 100f)
                        onBackPressed()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Save")
            }
        }
    }
}
