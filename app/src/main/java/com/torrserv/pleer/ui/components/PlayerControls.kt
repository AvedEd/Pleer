package com.torrserv.pleer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrserv.pleer.viewmodel.PlayerUiState

@Composable
fun PlayerControls(
    uiState: PlayerUiState,
    onPlayPause: () -> Unit,
    onFastForward: () -> Unit,
    onRewind: () -> Unit,
    onSeek: (Long) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onOpenBrowser: () -> Unit,
    onToggleControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                androidx.compose.foundation.shape.RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Player Controls",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Основные контролы
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(
                text = "⏪ -10s",
                onClick = onRewind
            )

            ControlButton(
                text = if (uiState.isPlaying) "⏸ Pause" else "▶ Play",
                onClick = onPlayPause,
                isPrimary = true
            )

            ControlButton(
                text = "⏩ +10s",
                onClick = onFastForward
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Скорость воспроизведения
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpeedButton("0.5x", 0.5f, uiState.playbackSpeed, onSpeedChange)
            SpeedButton("1.0x", 1.0f, uiState.playbackSpeed, onSpeedChange)
            SpeedButton("1.5x", 1.5f, uiState.playbackSpeed, onSpeedChange)
            SpeedButton("2.0x", 2.0f, uiState.playbackSpeed, onSpeedChange)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Доп. кнопки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButton(
                text = "📚 Browse",
                onClick = onOpenBrowser
            )
            ControlButton(
                text = "⚙ Settings",
                onClick = { /* Settings */ }
            )
        }
    }
}

@Composable
fun ControlButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        ),
        modifier = modifier
            .width(120.dp)
            .height(50.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isPrimary) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSecondary
            }
        )
    }
}

@Composable
fun SpeedButton(
    text: String,
    speed: Float,
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onSpeedChange(speed) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (currentSpeed == speed) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        modifier = modifier
            .width(80.dp)
            .height(40.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (currentSpeed == speed) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
