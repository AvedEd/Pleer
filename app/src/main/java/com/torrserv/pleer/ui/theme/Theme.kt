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
    secondaryContainer = Color(0xFF354B51),
    onSecondaryContainer = Color(0xFFCDEAF2),
    tertiary = Color(0xFFA5D6FF),
    onTertiary = Color(0xFF00264D),
    tertiaryContainer = Color(0xFF003D7A),
    onTertiaryContainer = Color(0xFFD0E2FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0A0E11),
    onBackground = Color(0xFFE0E2E5),
    surface = Color(0xFF121619),
    onSurface = Color(0xFFE0E2E5),
    surfaceVariant = Color(0xFF41484D),
    onSurfaceVariant = Color(0xFFC1C7CC),
    outline = Color(0xFF8B9297),
    outlineVariant = Color(0xFF41484D),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E2E5),
    inverseOnSurface = Color(0xFF2F3133),
    inversePrimary = Color(0xFF00677F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00677F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3EEFF),
    onPrimaryContainer = Color(0xFF001F2A),
    secondary = Color(0xFF354B51),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDEAF2),
    onSecondaryContainer = Color(0xFF0F2A30),
    tertiary = Color(0xFF003D7A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD0E2FF),
    onTertiaryContainer = Color(0xFF001155),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFAFBFC),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFDCE4E8),
    onSurfaceVariant = Color(0xFF41484D),
    outline = Color(0xFF72787E),
    outlineVariant = Color(0xFFC1C7CC),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2F3133),
    inverseOnSurface = Color(0xFFF1F0F1),
    inversePrimary = Color(0xFFB3EEFF)
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
