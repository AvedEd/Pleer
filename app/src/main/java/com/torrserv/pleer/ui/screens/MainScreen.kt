package com.torrserv.pleer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrserv.pleer.viewmodel.PlayerViewModel
import com.torrserv.pleer.ui.components.PlayerControls
import com.torrserv.pleer.ui.components.PlayerDisplay
import com.torrserv.pleer.ui.components.TorrservBrowser
import kotlinx.coroutines.delay

@Composable
fun MainScreen(viewModel: PlayerViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showBrowser by remember { mutableStateOf(false) }
    var autoHideControls by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Автоматически скрывать контролы через 5 секунд неактивности
    LaunchedEffect(showControls, lastInteractionTime) {
        if (autoHideControls && showControls) {
            delay(5000)
            if (System.currentTimeMillis() - lastInteractionTime > 5000) {
                showControls = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Главный экран плеера
        if (!showBrowser) {
            PlayerDisplay(
                uiState = uiState,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Браузер Torrserv
            TorrservBrowser(
                viewModel = viewModel,
                onStreamSelected = { url, title ->
                    viewModel.playMediaUrl(url, title)
                    showBrowser = false
                }
            )
        }

        // Контролы плеера
        if (showControls && !showBrowser) {
            PlayerControls(
                uiState = uiState,
                onPlayPause = {
                    viewModel.togglePlayPause()
                    lastInteractionTime = System.currentTimeMillis()
                },
                onFastForward = {
                    viewModel.fastForward()
                    lastInteractionTime = System.currentTimeMillis()
                },
                onRewind = {
                    viewModel.rewind()
                    lastInteractionTime = System.currentTimeMillis()
                },
                onSeek = { position ->
                    viewModel.seekTo(position)
                    lastInteractionTime = System.currentTimeMillis()
                },
                onSpeedChange = { speed ->
                    viewModel.setPlaybackSpeed(speed)
                    lastInteractionTime = System.currentTimeMillis()
                },
                onOpenBrowser = {
                    showBrowser = true
                    lastInteractionTime = System.currentTimeMillis()
                },
                onToggleControls = {
                    showControls = !showControls
                    lastInteractionTime = System.currentTimeMillis()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Отображение ошибок
        if (!uiState.errorMessage.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Индикатор буфера в углу
        Text(
            text = "Buffer: ${uiState.bufferingProgress}%",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }
}
