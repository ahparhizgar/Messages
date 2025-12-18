package org.fossify.messages.di

import org.koin.dsl.module
import org.fossify.messages.interfaces.*
import org.fossify.messages.providers.DatabaseProvider

/**
 * Koin module for database-related dependencies.
 * Provides individual DAOs from the DatabaseProvider.
 */
val dataModule = module {
    // Provide individual DAOs from DatabaseProvider
    single<ConversationsDao> { get<DatabaseProvider>().conversationsDao() }
    single<AttachmentsDao> { get<DatabaseProvider>().attachmentsDao() }
    single<MessageAttachmentsDao> { get<DatabaseProvider>().messageAttachmentsDao() }
    single<MessagesDao> { get<DatabaseProvider>().messagesDao() }
    single<DraftsDao> { get<DatabaseProvider>().draftsDao() }
    single<NotificationRuleDao> { get<DatabaseProvider>().notificationRuleDao() }
    single<NotificationCategoryDao> { get<DatabaseProvider>().notificationCategoryDao() }
}
