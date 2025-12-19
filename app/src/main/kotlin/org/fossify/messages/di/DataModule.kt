package org.fossify.messages.di

import org.koin.dsl.module
import org.fossify.messages.databases.MessagesDatabase
import org.fossify.messages.interfaces.*
import org.fossify.messages.providers.DatabaseProvider

/**
 * Koin module for database-related dependencies.
 * Provides the database instance and individual DAOs from it.
 */
val dataModule = module {
    // Provide the database - can be replaced with in-memory DB for testing
    single<MessagesDatabase> { get<DatabaseProvider>().getDatabase() }
    
    // Provide individual DAOs from the database
    single<ConversationsDao> { get<MessagesDatabase>().ConversationsDao() }
    single<AttachmentsDao> { get<MessagesDatabase>().AttachmentsDao() }
    single<MessageAttachmentsDao> { get<MessagesDatabase>().MessageAttachmentsDao() }
    single<MessagesDao> { get<MessagesDatabase>().MessagesDao() }
    single<DraftsDao> { get<MessagesDatabase>().DraftsDao() }
    single<NotificationRuleDao> { get<MessagesDatabase>().NotificationRuleDao() }
    single<NotificationCategoryDao> { get<MessagesDatabase>().NotificationCategoryDao() }
}
