package com.pixelrabbit.oculi.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A6B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF6FF7F8),
    onPrimaryContainer = Color(0xFF002021),

    secondary = Color(0xFF4A6363),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E8),
    onSecondaryContainer = Color(0xFF051F1F),

    tertiary = Color(0xFF4D5F7C),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD5E3FF),
    onTertiaryContainer = Color(0xFF061C36),

    background = Color(0xFFFAFDFC),
    onBackground = Color(0xFF191C1C),

    surface = Color(0xFFFAFDFC),
    onSurface = Color(0xFF191C1C),

    surfaceVariant = Color(0xFFDAE5E4),
    onSurfaceVariant = Color(0xFF3F4949),

    outline = Color(0xFF6F7979),
    outlineVariant = Color(0xFFBEC9C8),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    scrim = Color(0xFF000000),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DDADA),
    onPrimary = Color(0xFF003738),
    primaryContainer = Color(0xFF004F51),
    onPrimaryContainer = Color(0xFF6FF7F8),

    secondary = Color(0xFFB0CCCC),
    onSecondary = Color(0xFF1B3535),
    secondaryContainer = Color(0xFF324B4B),
    onSecondaryContainer = Color(0xFFCCE8E8),

    tertiary = Color(0xFFB5C7E8),
    onTertiary = Color(0xFF1D314D),
    tertiaryContainer = Color(0xFF344764),
    onTertiaryContainer = Color(0xFFD5E3FF),

    background = Color(0xFF191C1C),
    onBackground = Color(0xFFE0E3E2),

    surface = Color(0xFF191C1C),
    onSurface = Color(0xFFE0E3E2),

    surfaceVariant = Color(0xFF3F4949),
    onSurfaceVariant = Color(0xFFBEC9C8),

    outline = Color(0xFF889392),
    outlineVariant = Color(0xFF3F4949),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    scrim = Color(0xFF000000),
)

// Унифицированные формы
val OculiShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun OculiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OculiTypography,
        shapes = OculiShapes,
        content = content
    )
}