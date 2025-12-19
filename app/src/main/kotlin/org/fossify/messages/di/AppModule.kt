package org.fossify.messages.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.fossify.messages.providers.*

/**
 * Koin module for application-level dependencies.
 * Provides core services like preferences and database access.
 */
val appModule = module {
    // Preferences Provider
    single<PreferencesProvider> { PreferencesProviderImpl(androidContext()) }
    
    // Database Provider
    single<DatabaseProvider> { DatabaseProviderImpl(androidContext()) }
}
