package com.giovanna.amatucci.melisearch.di

import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClientImpl
import com.giovanna.amatucci.melisearch.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<NetworkHttpClient> {
        NetworkHttpClientImpl(
            baseHostUrl = BuildConfig.BASE_HOST_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE,
            applicationContext = androidContext()
        )
    }
}