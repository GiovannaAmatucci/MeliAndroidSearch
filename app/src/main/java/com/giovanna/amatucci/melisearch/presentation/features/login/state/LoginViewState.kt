package com.giovanna.amatucci.melisearch.presentation.features.login.state

sealed interface LoginViewState {
    data object Success : LoginViewState
    data class Error(val message: String) : LoginViewState
    object Loading : LoginViewState
}