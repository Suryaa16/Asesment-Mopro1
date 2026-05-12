package com.surya607062400013.asesmentmobpro1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//Cyber Color Palette
object CyberColors {
    val Background = Color(0xFF0A0A14)
    val Surface = Color(0xFF0D0D1A)
    val SurfaceVariant = Color(0xFF12122A)
    val Primary = Color(0xFFBB86FC)
    val Secondary = Color(0xFFBF00FF)
    val Tertiary = Color(0xFF39FF14)
    val Error = Color(0xFFFF1744)
    val OnBackground = Color(0xFFE0E0FF)
    val OnSurface = Color(0xFFE0E0FF)
    val OnSurfaceVariant = Color(0xFF8888AA)
    val OnPrimary = Color(0xFF000000)
    val Outline = Color(0xFF2A2A4A)
}

//Purple Cyber Scheme
private val PurpleCyberDark = darkColorScheme(
    primary = CyberColors.Primary,
    onPrimary = CyberColors.OnPrimary,
    primaryContainer = Color(0xFF003544),
    onPrimaryContainer = CyberColors.Primary,
    secondary = CyberColors.Secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF2A0044),
    onSecondaryContainer = CyberColors.Secondary,
    tertiary = CyberColors.Tertiary,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF003300),
    onTertiaryContainer = CyberColors.Tertiary,
    error = CyberColors.Error,
    onError = Color.White,
    background = CyberColors.Background,
    onBackground = CyberColors.OnBackground,
    surface = CyberColors.Surface,
    onSurface = CyberColors.OnSurface,
    surfaceVariant = CyberColors.SurfaceVariant,
    onSurfaceVariant = CyberColors.OnSurfaceVariant,
    outline = CyberColors.Outline
)

private val PurpleCyberLight = lightColorScheme(
    primary = Color(0xFF7B1FA2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A0072),
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8B4FF),
    onSecondaryContainer = Color(0xFF2A0044),
    tertiary = Color(0xFF2E7D32),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFA5D6A7),
    onTertiaryContainer = Color(0xFF003300),
    error = Color(0xFFB71C1C),
    onError = Color.White,
    background = Color(0xFFF0F4FF),
    onBackground = Color(0xFF0A0A2A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0A0A2A),
    surfaceVariant = Color(0xFFE8EAF6),
    onSurfaceVariant = Color(0xFF3A3A5A),
    outline = Color(0xFFBBBBDD)
)

//Blue Cyber
private val BlueCyberDark = darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0288D1),
    onPrimaryContainer = Color(0xFF00E5FF),
    secondary = Color(0xFF00E5FF),
    secondaryContainer = Color(0xFF0288D1),
    tertiary = Color(0xFF39FF14),
    error = CyberColors.Error,
    background = CyberColors.Background,
    onBackground = CyberColors.OnBackground,
    surface = CyberColors.Surface,
    onSurface = CyberColors.OnSurface,
    surfaceVariant = CyberColors.SurfaceVariant,
    onSurfaceVariant = CyberColors.OnSurfaceVariant,
    outline = CyberColors.Outline
)

private val BlueCyberLight = lightColorScheme(
    primary = Color(0xFF0288D1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB3E5FC),
    onPrimaryContainer = Color(0xFF01579B),
    secondary = Color(0xFF0097A7),
    secondaryContainer = Color(0xFFC2EFFF),
    tertiary = Color(0xFF2E7D32),
    background = Color(0xFFF0F8FF),
    onBackground = Color(0xFF0A1A2A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0A1A2A),
    surfaceVariant = Color(0xFFE3F2FD),
    onSurfaceVariant = Color(0xFF2A4A5A)
)

// Green Cyber
private val GreenCyberDark = darkColorScheme(
    primary = Color(0xFF39FF14),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color(0xFF39FF14),
    secondary = Color(0xFF39FF14),
    secondaryContainer = Color(0xFF2E7D32),
    tertiary = Color(0xFFFFD600),
    error = CyberColors.Error,
    background = CyberColors.Background,
    onBackground = CyberColors.OnBackground,
    surface = CyberColors.Surface,
    onSurface = CyberColors.OnSurface,
    surfaceVariant = CyberColors.SurfaceVariant,
    onSurfaceVariant = CyberColors.OnSurfaceVariant,
    outline = CyberColors.Outline
)

private val GreenCyberLight = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF003300),
    secondary = Color(0xFF00796B),
    secondaryContainer = Color(0xFFC8E6C9),
    tertiary = Color(0xFFF57F17),
    background = Color(0xFFF0FFF0),
    onBackground = Color(0xFF0A1A0A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0A1A0A),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF2A4A2A)
)

//Orange Cyber
private val OrangeCyberDark = darkColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color(0xFFFF9800),
    secondary = Color(0xFFE65100),
    secondaryContainer = Color(0xFFE65100),
    tertiary = Color(0xFFFFD600),
    error = CyberColors.Error,
    background = CyberColors.Background,
    onBackground = CyberColors.OnBackground,
    surface = CyberColors.Surface,
    onSurface = CyberColors.OnSurface,
    surfaceVariant = CyberColors.SurfaceVariant,
    onSurfaceVariant = CyberColors.OnSurfaceVariant,
    outline = CyberColors.Outline
)

private val OrangeCyberLight = lightColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCC80),
    onPrimaryContainer = Color(0xFF331100),
    secondary = Color(0xFFE65100),
    secondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFFF57F17),
    background = Color(0xFFFFF8F0),
    onBackground = Color(0xFF1A0A00),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A0A00),
    surfaceVariant = Color(0xFFFFF3E0),
    onSurfaceVariant = Color(0xFF4A2A0A)
)
@Composable
fun FitCallTheme(
    darkTheme: Boolean = false,
    themeColor: String = "Purple",
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeColor) {
        "Purple" -> if (darkTheme) PurpleCyberDark else PurpleCyberLight
        "Blue" -> if (darkTheme) BlueCyberDark else BlueCyberLight
        "Green" -> if (darkTheme) GreenCyberDark else GreenCyberLight
        "Orange" -> if (darkTheme) OrangeCyberDark else OrangeCyberLight
        else -> if (darkTheme) PurpleCyberDark else PurpleCyberLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}