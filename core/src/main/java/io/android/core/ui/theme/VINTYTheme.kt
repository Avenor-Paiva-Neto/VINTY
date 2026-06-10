package io.android.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MushroomLightColorScheme = lightColorScheme(
    primary = MushroomPrimary,
    onPrimary = MushroomOnPrimary,
    primaryContainer = MushroomPrimaryContainer,
    secondary = MushroomSecondary,
    onSecondary = MushroomOnSecondary,
    background = MushroomBackground,
    surface = MushroomSurface,
    onSurface = MushroomOnSurface,
    outline = MushroomOutline
)

// No VINTY, o Dark Theme segue tons de terra profundos
private val MushroomDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE7BEAF),
    secondary = Color(0xFFC6C9A7),
    background = Color(0xFF201A18),
    surface = Color(0xFF201A18),
    onSurface = Color(0xFFEDE0DD)
)

val VintyBlack = Color(0xFF000000)
val VintyDarkGray = Color(0xFF121212)
val VintyCardSurface = Color(0xFF1E1E1E)
val VintyPillBackground = Color(0xFF2A2A2A) // A cor da pílula "Continuar"
val VintyGlass = Color(0x66000000) // Preto com 40% opacidade

@Composable
fun VINTYTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Ativa cores dinâmicas do Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> MushroomDarkColorScheme
        else -> MushroomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}