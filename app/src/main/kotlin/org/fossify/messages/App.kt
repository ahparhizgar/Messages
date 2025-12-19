package org.fossify.messages

import org.fossify.commons.FossifyApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.fossify.messages.di.appModule
import org.fossify.messages.di.dataModule
import org.fossify.messages.di.viewModelModule

class App : FossifyApp() {
    override val isAppLockFeatureAvailable = true

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                appModule,
                dataModule,
                viewModelModule
            )
        }
    }
}
