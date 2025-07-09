package com.giovanna.amatucci.melisearch.domain.repository

import com.giovanna.amatucci.melisearch.data.model.TokenResponse
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper

interface OauthRepository {
    suspend fun postAccessToken(redirectUri: String, code: String, codeVerifier: String): ResultWrapper<TokenResponse>
}