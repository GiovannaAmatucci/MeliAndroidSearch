package com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


/**
 * Componente de texto padronizado para o título principal de um Card.
 *
 * Utiliza a tipografia `bodyLarge` e a cor `onSurface` do tema.
 *
 * @param text O texto a ser exibido.
 * @param modifier O modificador a ser aplicado.
 * @param maxLines O número máximo de linhas.
 */
@Composable
fun CardTitleText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center
    )
}

/**
 * Componente de texto padronizado para o subtítulo ou descrição de um Card.
 *
 * Utiliza a tipografia `bodySmall` e a cor `onSurfaceVariant` do tema para um contraste sutil.
 *
 * @param text O texto a ser exibido.
 * @param modifier O modificador a ser aplicado.
 * @param maxLines O número máximo de linhas.
 */
@Composable
fun CardSubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 3,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}