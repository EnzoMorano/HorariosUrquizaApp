package com.example.horariosurquiza.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = OnYellowPrimary,
    primaryContainer = YellowPrimaryContainer,
    onPrimaryContainer = OnYellowPrimaryContainer,

    secondary = SlateSecondary,
    onSecondary = OnSlateSecondary,
    secondaryContainer = SlateSecondaryContainer,
    onSecondaryContainer = OnSlateSecondaryContainer,

    background = LightBackground,
    onBackground = CharcoalDark,
    surface = LightSurface,
    onSurface = CharcoalDark,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = SlateSecondary,
)

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = OnYellowPrimary,
    primaryContainer = Color(0xFF3E3000),
    onPrimaryContainer = Color(0xFFFFE071),

    secondary = Color(0xFF94A3B8),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFE2E8F0),

    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
)

@Composable
fun HorariosUrquizaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado por defecto para mantener la identidad visual con #FCCD06
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
