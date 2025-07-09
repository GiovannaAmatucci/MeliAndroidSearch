package com.giovanna.amatucci.melisearch.presentation.features.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.melisearch.BuildConfig
import com.giovanna.amatucci.melisearch.presentation.features.login.content.LoginContent
import com.giovanna.amatucci.melisearch.presentation.features.login.state.LoginViewState
import com.giovanna.amatucci.melisearch.presentation.utils.CLIENT_ID
import com.giovanna.amatucci.melisearch.presentation.utils.CODE
import com.giovanna.amatucci.melisearch.presentation.utils.CODE_CHALLENGE
import com.giovanna.amatucci.melisearch.presentation.utils.CODE_CHALLENGE_METHOD
import com.giovanna.amatucci.melisearch.presentation.utils.MELI_REDIRECT_URI
import com.giovanna.amatucci.melisearch.presentation.utils.REDIRECT_URI
import com.giovanna.amatucci.melisearch.presentation.utils.RESPONSE_TYPE
import com.giovanna.amatucci.melisearch.presentation.utils.URL_AUTHORIZATION
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel(), navigateToHome: () -> Unit) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.preparePkceParams()
    }
    val pkceParams by viewModel.pkceAuthUrlParams.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (state) {
            is LoginViewState.Success -> navigateToHome()
            is LoginViewState.Error -> {
                snackBarHostState.showSnackbar(
                    message = (state as LoginViewState.Error).message
                )
            }
            is LoginViewState.Loading -> {}
        }
    }
    val authUrl = pkceParams?.let { params ->
        URL_AUTHORIZATION.toUri()
            .buildUpon()
            .appendQueryParameter(RESPONSE_TYPE, CODE)
            .appendQueryParameter(CLIENT_ID, BuildConfig.PUBLIC_KEY)
            .appendQueryParameter(REDIRECT_URI, MELI_REDIRECT_URI)
            .appendQueryParameter(CODE_CHALLENGE, params.codeChallenge)
            .appendQueryParameter(CODE_CHALLENGE_METHOD, params.codeChallengeMethod)
            .build()
            .toString()
    }
    LoginContent(
        authUrl = authUrl,
        snackBarHostState = snackBarHostState,
        onAuthCodeReceived = { code ->
            viewModel.fetchAccessToken(MELI_REDIRECT_URI, code)
        }
    )
}


