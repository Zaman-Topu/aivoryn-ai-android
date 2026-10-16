package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    AMOLED
}

val ClaudeDarkColorScheme = darkColorScheme(
    primary = ClaudeCoral,
    onPrimary = ColorTextInverseDark,
    primaryContainer = ClaudeCoralContainer,
    onPrimaryContainer = ClaudeCoralLight,
    secondary = ColorTextSecondaryDark,
    onSecondary = ColorTextInverseDark,
    tertiary = ColorTextTertiaryDark,
    onTertiary = ColorTextInverseDark,
    background = ColorBackgroundTertiaryDark,
    onBackground = ColorTextPrimaryDark,
    surface = ColorBackgroundPrimaryDark,
    onSurface = ColorTextPrimaryDark,
    surfaceVariant = ColorBackgroundSecondaryDark,
    onSurfaceVariant = ColorTextSecondaryDark,
    outline = ColorBorderPrimaryDark,
    outlineVariant = ColorBorderTertiaryDark,
    error = ColorTextDangerDark,
    onError = ColorTextInverseDark,
    errorContainer = ColorBackgroundDangerContainerDark,
    onErrorContainer = ColorTextDangerDark
)

val ClaudeLightColorScheme = lightColorScheme(
    primary = ClaudeCoral,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFDE8E1),
    onPrimaryContainer = ClaudeCoralDark,
    secondary = ColorTextSecondaryLight,
    onSecondary = Color.White,
    tertiary = ColorTextTertiaryLight,
    onTertiary = Color.White,
    background = ColorBackgroundTertiaryLight,
    onBackground = ColorTextPrimaryLight,
    surface = ColorBackgroundPrimaryLight,
    onSurface = ColorTextPrimaryLight,
    surfaceVariant = ColorBackgroundSecondaryLight,
    onSurfaceVariant = ColorTextSecondaryLight,
    outline = ColorBorderPrimaryLight,
    outlineVariant = ColorBorderTertiaryLight,
    error = ColorTextDangerLight,
    onError = Color.White,
    errorContainer = Color(0xFFFDF2F2),
    onErrorContainer = ColorTextDangerLight
)

val ClaudeAmoledColorScheme = darkColorScheme(
    primary = ClaudeCoral,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1C1412),
    onPrimaryContainer = ClaudeCoralLight,
    secondary = ColorTextSecondaryDark,
    onSecondary = Color.Black,
    tertiary = ColorTextTertiaryDark,
    onTertiary = Color.Black,
    background = Color(0xFF000000),
    onBackground = ColorTextPrimaryDark,
    surface = Color(0xFF0F0F0E),
    onSurface = ColorTextPrimaryDark,
    surfaceVariant = Color(0xFF171716),
    onSurfaceVariant = ColorTextSecondaryDark,
    outline = ColorBorderTertiaryDark,
    outlineVariant = ColorBorderDisabledDark,
    error = ColorTextDangerDark,
    onError = Color.Black,
    errorContainer = ColorBackgroundDangerContainerDark,
    onErrorContainer = ColorTextDangerDark
)

@Composable
fun AivorynAITheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemInDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AMOLED -> true
    }

    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeMode == ThemeMode.AMOLED -> ClaudeAmoledColorScheme
        isDark -> ClaudeDarkColorScheme
        else -> ClaudeLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
