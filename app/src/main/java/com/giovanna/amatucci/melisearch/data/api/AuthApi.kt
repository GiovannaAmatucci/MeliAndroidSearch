package com.giovanna.amatucci.melisearch.data.api

import com.giovanna.amatucci.melisearch.data.model.TokenResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult

interface AuthApi {
    suspend fun postAccessToken(
        redirectUri: String,
        code: String,
        codeVerifier: String
    ): ApiResult<TokenResponse>

    suspend fun refreshAccessToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): ApiResult<TokenResponse>
}