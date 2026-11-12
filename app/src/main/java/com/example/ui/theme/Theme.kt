package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
  DARK,
  AMOLED,
  LIGHT
}

private val DarkColorScheme = darkColorScheme(
  primary = CinemaRed,
  onPrimary = Color.White,
  primaryContainer = CinemaRedDark,
  onPrimaryContainer = Color(0xFFFFDAD6),
  secondary = CinemaGold,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF3E2D00),
  onSecondaryContainer = Color(0xFFFFDF9E),
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkSurfaceBorder,
  error = Color(0xFFFF5449)
)

private val AmoledColorScheme = darkColorScheme(
  primary = CinemaRedVariant,
  onPrimary = Color.White,
  primaryContainer = Color(0xFF4D0005),
  onPrimaryContainer = Color(0xFFFFDAD6),
  secondary = CinemaGold,
  onSecondary = Color.Black,
  background = AmoledBackground,
  onBackground = Color.White,
  surface = AmoledSurface,
  onSurface = Color.White,
  surfaceVariant = AmoledElevated,
  onSurfaceVariant = DarkTextSecondary,
  outline = Color(0xFF26262E),
  error = Color(0xFFFF5449)
)

private val LightColorScheme = lightColorScheme(
  primary = CinemaRed,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFFDAD6),
  onPrimaryContainer = Color(0xFF410003),
  secondary = CinemaAmber,
  onSecondary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = LightTextSecondary,
  outline = LightSurfaceBorder,
  error = Color(0xFFBA1A1A)
)

@Composable
fun SavelevTvTheme(
  themeMode: AppThemeMode = AppThemeMode.DARK,
  content: @Composable () -> Unit
) {
  val colorScheme = when (themeMode) {
    AppThemeMode.DARK -> DarkColorScheme
    AppThemeMode.AMOLED -> AmoledColorScheme
    AppThemeMode.LIGHT -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
