package com.giovanna.amatucci.melisearch.data.network

import com.giovanna.amatucci.melisearch.data.db.TokenDao
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

class NetworkHttpClientImpl(
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val isDebug: Boolean,
    private val tokenDao: TokenDao
) : NetworkHttpClient {

    override operator fun invoke(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenDao.getToken()?.accessToken
                    val refreshToken = tokenDao.getToken()?.refreshToken
                    if (token != null) {
                        BearerTokens(accessToken = token, refreshToken = refreshToken)
                    } else {
                        null
                    }
                }
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            url {
                protocol = URLProtocol.HTTPS
                host = baseHostUrl
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = requestTimeout
            connectTimeoutMillis = connectTimeout
            socketTimeoutMillis = connectTimeout
        }

        install(Logging) {
            level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    if (isDebug) {
                        Timber.d(message)
                    }
                }
            }
        }
    }
}