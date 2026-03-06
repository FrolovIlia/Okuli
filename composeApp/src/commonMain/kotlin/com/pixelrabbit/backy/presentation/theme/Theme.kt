package com.pixelrabbit.backy.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF8F2B),           // основной оранжевый (жирафик)
    onPrimary = Color(0xFFFFFFFF),

    primaryContainer = Color(0xFFFFD7B3),
    onPrimaryContainer = Color(0xFF3B1F00),

    secondary = Color(0xFF4CAF7D),         // здоровье / упражнения
    onSecondary = Color(0xFFFFFFFF),

    secondaryContainer = Color(0xFFDFF6EA), // карточки типа "Быстрый старт"
    onSecondaryContainer = Color(0xFF002114),

    tertiary = Color(0xFF6EC6D9),          // мягкий бирюзовый акцент
    onTertiary = Color(0xFF00363F),

    tertiaryContainer = Color(0xFFCFF2F9),
    onTertiaryContainer = Color(0xFF001F26),

    background = Color(0xFFFFFBF7),        // тёплый фон
    onBackground = Color(0xFF1F1B16),

    surface = Color(0xFFFFFBF7),
    onSurface = Color(0xFF1F1B16),

    surfaceVariant = Color(0xFFF1E0D2),    // статистика / блоки
    onSurfaceVariant = Color(0xFF52443A),

    outline = Color(0xFF857469),
    outlineVariant = Color(0xFFD8C3B6),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),

    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    scrim = Color(0xFF000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB86B),
    onPrimary = Color(0xFF4A2800),

    primaryContainer = Color(0xFF6A3E00),
    onPrimaryContainer = Color(0xFFFFD7B3),

    secondary = Color(0xFF88D6A8),
    onSecondary = Color(0xFF003921),

    secondaryContainer = Color(0xFF1F4F38),
    onSecondaryContainer = Color(0xFFDFF6EA),

    tertiary = Color(0xFF8EDCEB),
    onTertiary = Color(0xFF00363F),

    tertiaryContainer = Color(0xFF004E59),
    onTertiaryContainer = Color(0xFFCFF2F9),

    background = Color(0xFF1B1712),
    onBackground = Color(0xFFECE1D8),

    surface = Color(0xFF1B1712),
    onSurface = Color(0xFFECE1D8),

    surfaceVariant = Color(0xFF52443A),
    onSurfaceVariant = Color(0xFFD8C3B6),

    outline = Color(0xFFA08D80),
    outlineVariant = Color(0xFF52443A),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),

    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    scrim = Color(0xFF000000)
)

val backyShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun backyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme.copy(surfaceTint = Color.Transparent),
        typography = backyTypography,
        shapes = backyShapes,
        content = content
    )
}
