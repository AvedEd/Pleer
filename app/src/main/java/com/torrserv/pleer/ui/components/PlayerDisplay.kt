package com.torrserv.pleer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrserv.pleer.viewmodel.PlayerUiState
import java.util.concurrent.TimeUnit

@Composable
fun PlayerDisplay(
    uiState: PlayerUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = uiState.currentTitle,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Статус воспроизведения
            Text(
                text = if (uiState.isPlaying) "▶ Playing" else "⏸ Paused",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Прогресс-бар
            ProgressBar(
                progress = if (uiState.duration > 0) {
                    (uiState.currentPosition.toFloat() / uiState.duration)
                } else 0f,
                bufferedProgress = if (uiState.duration > 0) {
                    (uiState.bufferedPosition.toFloat() / uiState.duration)
                } else 0f,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp)
                    .padding(vertical = 16.dp)
            )

            // Время
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(uiState.currentPosition),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = formatTime(uiState.duration),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Скорость воспроизведения
            Text(
                text = "Speed: ${String.format("%.1f", uiState.playbackSpeed)}x",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    bufferedProgress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
    ) {
        // Буферизованный прогресс
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(bufferedProgress.coerceIn(0f, 1f))
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
        )

        // Текущий прогресс
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(
                    MaterialTheme.colorScheme.primary,
                    androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
        )
    }
}

private fun formatTime(milliseconds: Long): String {
    if (milliseconds < 0) return "00:00:00"
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
