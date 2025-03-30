package ru.practicum.android.diploma.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    background = Black,
    surface = Black,    // back top app bar
    surfaceContainer = Black,   //back bottom bar
    onBackground = White, // text Text()
    onSurface = White,  //text topbar
    onSurfaceVariant = White,  // placeholder text field
    surfaceContainerHighest = Gray,  // back text field


    primary = Color.Red,
    secondary = Color.Red,
    tertiary = Color.Red,
    onPrimary = Color.Red,
    onSecondary = Color.Red,
    onTertiary = Color.Red,
    primaryContainer = Color.Red,
    onPrimaryContainer = Color.Red,
    inversePrimary = Color.Red,
    secondaryContainer = Color.Red,
    onSecondaryContainer = Color.Red,
    tertiaryContainer = Color.Red,
    onTertiaryContainer = Color.Red,
    surfaceVariant = Color.Red,
    surfaceTint = Color.Red,

    inverseSurface = Color.Red,
    inverseOnSurface = Color.Red,

    error = Color.Red,
    onError = Color.Red,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,

    outline = Color.Red,
    outlineVariant = Color.Red,
    scrim = Color.Red,
    surfaceBright = Color.Red,
    surfaceContainerHigh = Color.Red,
    surfaceContainerLow = Color.Red,
    surfaceContainerLowest = Color.Red,
    surfaceDim = Color.Red,
)

private val LightColorScheme = lightColorScheme(
    background = White,
    surface = White,
    surfaceContainer = White,
    onBackground = Black,
    onSurface = Black,
    onSurfaceVariant = Gray,
    surfaceContainerHighest = LightGray,


    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,


    )

@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
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
