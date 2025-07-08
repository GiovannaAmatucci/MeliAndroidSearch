package com.giovanna.amatucci.melisearch.di

import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClientImpl
import com.giovanna.amatucci.melisearch.data.api.MeliApi
import com.giovanna.amatucci.melisearch.data.api.MeliApiImpl
import com.giovanna.amatucci.melisearch.data.api.AuthApi
import com.giovanna.amatucci.melisearch.data.api.AuthApiImpl
import com.giovanna.amatucci.melisearch.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<CoroutineDispatchers> { CoroutineDispatchersImpl() }
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().TokenDao() }
    single { get<AppDatabase>().SearchDao() }
    single<NetworkHttpClient> {
        NetworkHttpClientImpl(
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE,
            tokenDao = get()
        )
    }
    single<MeliApi> { MeliApiImpl(client = get()) }
    single<AuthApi> { AuthApiImpl(client = get()) }
}
