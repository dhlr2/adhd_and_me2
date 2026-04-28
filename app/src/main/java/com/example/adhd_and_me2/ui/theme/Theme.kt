package com.example.adhd_and_me2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = Teal600,
    onPrimary        = OffWhite,
    primaryContainer = Teal100,
    onPrimaryContainer = MintText,

    secondary        = Teal400,
    onSecondary      = MintText,
    secondaryContainer = Teal50,
    onSecondaryContainer = MintText,

    background       = OffWhite,
    onBackground     = MintText,

    surface          = OffWhite,
    onSurface        = MintText,
    onSurfaceVariant = SubtleGray,
)

private val DarkColorScheme = darkColorScheme(
    primary          = Teal200,
    onPrimary        = MintText,
    primaryContainer = Teal800,
    onPrimaryContainer = MintLight,

    secondary        = Teal400,
    onSecondary      = MintText,
    secondaryContainer = Teal800,
    onSecondaryContainer = MintLight,

    background       = MintText,
    onBackground     = Teal50,

    surface          = Teal800,
    onSurface        = Teal50,
    onSurfaceVariant = Teal100,
)

@Composable
fun AdhdandmeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic colour so teal palette always shows consistently
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    // Tint the status bar to match the app
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}