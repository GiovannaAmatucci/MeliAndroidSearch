package com.giovanna.amatucci.melisearch.presentation.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.melisearch.domain.model.PkceAuthUrlParams
import com.giovanna.amatucci.melisearch.domain.repository.OauthRepository
import com.giovanna.amatucci.melisearch.domain.usecase.GeneratePkceUseCase
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import com.giovanna.amatucci.melisearch.presentation.features.login.state.LoginViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: OauthRepository,
    private val useCase: GeneratePkceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<LoginViewState>(LoginViewState.Loading)
    val state: StateFlow<LoginViewState> = _state

    private var currentCodeVerifier: String? = null
    private val _pkceAuthUrlParams = MutableStateFlow<PkceAuthUrlParams?>(null)
    val pkceAuthUrlParams: StateFlow<PkceAuthUrlParams?> = _pkceAuthUrlParams.asStateFlow()

    fun preparePkceParams() {
        val pkce = useCase()
        currentCodeVerifier = pkce.codeVerifier
        _pkceAuthUrlParams.value = PkceAuthUrlParams(
            codeChallenge = pkce.codeChallenge,
            codeChallengeMethod = pkce.codeChallengeMethod
        )
    }

    fun fetchAccessToken(redirectUri: String, code: String) {
        val verifier = currentCodeVerifier
        if (verifier == null) {
            _state.value = LoginViewState.Error(String())
            return
        }
        viewModelScope.launch {
            _state.value = LoginViewState.Loading
            val response =
                repository.postAccessToken(redirectUri, code, verifier)
            when (response) {
                is ResultWrapper.Success -> {
                    _state.value = LoginViewState.Success
                    currentCodeVerifier = null
                }
                is ResultWrapper.Error -> {
                    _state.value = LoginViewState.Error(response.message.orEmpty())
                }
                else -> {
                    _state.value = LoginViewState.Loading
                }
            }
        }
    }
}
