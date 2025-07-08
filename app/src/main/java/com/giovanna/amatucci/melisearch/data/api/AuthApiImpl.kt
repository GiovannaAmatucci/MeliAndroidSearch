package com.giovanna.amatucci.melisearch.data.api

import com.giovanna.amatucci.melisearch.BuildConfig
import com.giovanna.amatucci.melisearch.data.model.TokenResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf

class AuthApiImpl(private val client: NetworkHttpClient) : AuthApi {
    override suspend fun postAccessToken(
        redirectUri: String,
        code: String,
        codeVerifier: String
    ): ApiResult<TokenResponse> {
        return try {
            val response = client().post("oauth/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parametersOf(
                            "grant_type" to listOf("authorization_code"),
                            "client_id" to listOf(BuildConfig.PUBLIC_KEY),
                            "client_secret" to listOf(BuildConfig.PRIVATE_KEY),
                            "code" to listOf(code),
                            "code_verifier" to listOf(codeVerifier),
                            "redirect_uri" to listOf(redirectUri)
                        )
                    )
                )
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (t: Throwable) {
            ApiResult.Exception(t)
        }
    }

    override suspend fun refreshAccessToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): ApiResult<TokenResponse> {
        return try {
            val response = client().post("oauth/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parametersOf(
                            "grant_type" to listOf("refresh_token"),
                            "client_id" to listOf(clientId),
                            "client_secret" to listOf(clientSecret),
                            "refresh_token" to listOf(refreshToken)
                        )
                    )
                )
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (t: Throwable) {
            ApiResult.Exception(t)
        }
    }
}