package org.fossify.messages.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.fossify.messages.viewmodels.NotificationRulesViewModel

/**
 * Koin module for ViewModels.
 * Provides ViewModel instances with automatic dependency injection.
 */
val viewModelModule = module {
    viewModel { NotificationRulesViewModel(get(), get()) }
}
