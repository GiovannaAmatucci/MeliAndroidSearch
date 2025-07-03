package com.giovanna.amatucci.melisearch.data.network

import io.ktor.client.HttpClient

interface NetworkHttpClient {
    operator fun invoke(): HttpClient
}