package com.giovanna.amatucci.melisearch.presentation.features.home.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.melisearch.R
import com.giovanna.amatucci.melisearch.ui.theme.MercadoLivreColors

@Composable
fun HomeInitialState() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.home_img),
            contentDescription = null,
            modifier = Modifier,
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.home_initial_state_text),
            style = MaterialTheme.typography.titleMedium,
            color = MercadoLivreColors.meliLightBlue,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            stringResource(R.string.home_initial_state_subtext),
            style = MaterialTheme.typography.bodyMedium,
            color = MercadoLivreColors.mediumGray,
            textAlign = TextAlign.Center
        )

    }
}