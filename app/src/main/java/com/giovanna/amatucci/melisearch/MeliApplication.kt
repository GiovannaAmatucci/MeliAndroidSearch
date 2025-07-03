package com.giovanna.amatucci.melisearch

import android.app.Application
import com.giovanna.amatucci.melisearch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MeliApplication : Application() {
    override fun onCreate() {
        setupTimber()
        setupKoin()
        super.onCreate()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG_MODE) {
            Timber.Forest.plant(Timber.DebugTree())
            Timber.Forest.d("Timber Initialized.")
        }
    }
    private fun setupKoin() {
        startKoin {
            if (BuildConfig.DEBUG_MODE) androidLogger(Level.DEBUG) else androidLogger(Level.NONE)
            androidContext(this@MeliApplication)
            modules(appModule)
        }
        Timber.Forest.d("Koin Initialized.")
    }
}