package com.giovanna.amatucci.melisearch.presentation.features.login.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.giovanna.amatucci.melisearch.presentation.features.login.AndroidWebViewDetail
import com.giovanna.amatucci.melisearch.presentation.utils.MELI_REDIRECT_URI
import com.giovanna.amatucci.melisearch.ui.theme.MercadoLivreColors

/**
 * @param authUrl A URL completa para carregar no WebView. Nula se ainda não estiver pronta.
 * @param onAuthCodeReceived Lambda que é chamada quando o código de autorização é recebido do WebView.
 */
@Composable
@ExperimentalMaterial3Api
fun LoginContent(
    authUrl: String?,
    snackBarHostState: SnackbarHostState,
    onAuthCodeReceived: (code: String) -> Unit
) {
    var isWebViewPageLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MercadoLivreColors.meliLightYellow)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MercadoLivreColors.meliLightYellow)
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { paddingValues ->
            if (authUrl != null) {
                AndroidWebViewDetail(
                    urlToLoad = authUrl,
                    redirectUri = MELI_REDIRECT_URI,
                    onCodeReceived = onAuthCodeReceived,
                    onLoadingStateChanged = { isLoading ->
                        isWebViewPageLoading = isLoading
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .alpha(if (isWebViewPageLoading) 0f else 1f)
                )
            }
        }
        if (authUrl == null || isWebViewPageLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}