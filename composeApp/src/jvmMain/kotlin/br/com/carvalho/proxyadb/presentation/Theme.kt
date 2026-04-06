package br.com.carvalho.proxyadb.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    val background = Color(0xFF0F0F13)
    val surface = Color(0xFF1A1A24)
    val surfaceDark = Color(0xFF12121A)
    val surfaceBorder = Color(0xFF2A2A3A)
    val textPrimary = Color(0xFFF0EEF8)
    val textSecondary = Color(0xFF8A88A0)
    val mono = Color(0xFF6EE7B7)
    val success = Color(0xFF3DDB84)
    val successDim = Color(0xFF1A6640)
    val close = Color(0xFFFF0000)
    val error = Color(0xFFE05C5C)
}

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.success,
    onPrimary = Color.Black,
    secondary = AppColors.mono,
    onSecondary = Color.Black,
    background = AppColors.background,
    onBackground = AppColors.textPrimary,
    surface = AppColors.surface,
    onSurface = AppColors.textPrimary,
    surfaceVariant = AppColors.surfaceDark,
    onSurfaceVariant = AppColors.textSecondary,
    outline = AppColors.surfaceBorder,
    error = AppColors.error,
    onError = Color.Black,
)

@Composable
fun ProxyADBTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
