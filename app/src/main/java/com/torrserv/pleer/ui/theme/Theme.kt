package com.torrserv.pleer.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D9FF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF006B7D),
    onPrimaryContainer = Color(0xFFB3EEFF),
    secondary = Color(0xFFB1CDD6),
    onSecondary = Color(0xFF1F3439),
    background = Color(0xFF0A0E11),
    onBackground = Color(0xFFE0E2E5),
    surface = Color(0xFF121619),
    onSurface = Color(0xFFE0E2E5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00677F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3EEFF),
    onPrimaryContainer = Color(0xFF001F2A),
    secondary = Color(0xFF354B51),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFAFBFC),
    onSurface = Color(0xFF191C1E)
)

@Composable
fun PleerTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
