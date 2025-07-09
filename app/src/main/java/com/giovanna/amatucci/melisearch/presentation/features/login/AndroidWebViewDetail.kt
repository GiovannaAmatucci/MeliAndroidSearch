package com.giovanna.amatucci.melisearch.presentation.features.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

/**
 * Um Composable que exibe a WebView para o fluxo de autorização do Mercado Livre.
 *
 * @param urlToLoad A URL de autorização inicial a ser carregada.
 * @param redirectUri A URI de redirecionamento que sinaliza o fim do fluxo.
 * @param onCodeReceived Callback que é invocado com o código de autorização quando ele é recebido.
 * @param modifier Modificador para o Composable.
 * @param onLoadingStateChanged Callback que é invocado quando o estado de carregamento da WebView muda.
 *
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AndroidWebViewDetail(
    urlToLoad: String,
    redirectUri: String,
    onCodeReceived: (code: String) -> Unit,
    onLoadingStateChanged: (isLoading: Boolean) -> Unit, // <-- 1. NOVO PARÂMETRO
    modifier: Modifier = Modifier
) {
    val latestOnCodeReceived by rememberUpdatedState(onCodeReceived)
    val latestOnLoadingStateChanged by rememberUpdatedState(onLoadingStateChanged)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        latestOnLoadingStateChanged(true)
                    }
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        latestOnLoadingStateChanged(false)
                    }
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        latestOnLoadingStateChanged(false)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val currentUrl = request?.url?.toString().orEmpty()

                        if (currentUrl.startsWith(redirectUri)) {
                            val code = currentUrl.toUri().getQueryParameter("code")
                            if (code != null) {
                                latestOnCodeReceived(code)
                                return true
                            }
                        }
                        return false
                    }
                }
            }
        },
        update = { webView ->
            webView.loadUrl(urlToLoad)
        }
    )
}
