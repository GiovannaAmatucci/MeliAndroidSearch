package com.giovanna.amatucci.melisearch.data.network

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
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
import okhttp3.Cache
import timber.log.Timber
import java.io.File

class NetworkHttpClientImpl(
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val isDebug: Boolean,
    private val applicationContext: Context
) : NetworkHttpClient {

    override operator fun invoke(): HttpClient = HttpClient(OkHttp) {
        engine {
            config {
                cache(
                    Cache(
                        directory = File(applicationContext.cacheDir, "http_cache"),
                        maxSize = 50L * 1024L * 1024L
                    )
                )
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
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
            level = if (isDebug) LogLevel.BODY else LogLevel.NONE
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