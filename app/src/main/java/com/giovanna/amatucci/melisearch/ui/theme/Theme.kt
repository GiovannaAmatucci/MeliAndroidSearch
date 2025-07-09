package com.giovanna.amatucci.melisearch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val MercadoLivreColorScheme = lightColorScheme(
    primary = MercadoLivreColors.meliYellow,
    onPrimary = MercadoLivreColors.darkGray,
    primaryContainer = MercadoLivreColors.meliLightYellow,
    onPrimaryContainer = MercadoLivreColors.darkGray,
    secondary = MercadoLivreColors.meliBlue,
    onSecondary = MercadoLivreColors.white,
    background = MercadoLivreColors.white,
    onBackground = MercadoLivreColors.black,
    surface = MercadoLivreColors.white,
    onSurface = MercadoLivreColors.black,
    surfaceVariant = MercadoLivreColors.white,
    error = MercadoLivreColors.errorRed,
    onError = MercadoLivreColors.white
)

@Composable
fun MeliTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = MercadoLivreColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}