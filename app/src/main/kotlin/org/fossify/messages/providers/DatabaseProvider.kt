package org.fossify.messages.providers

import org.fossify.messages.interfaces.*

/**
 * Interface for providing access to database DAOs.
 * This abstraction enables easy mocking in tests and decouples from Room implementation.
 */
interface DatabaseProvider {
    fun conversationsDao(): ConversationsDao
    fun attachmentsDao(): AttachmentsDao
    fun messageAttachmentsDao(): MessageAttachmentsDao
    fun messagesDao(): MessagesDao
    fun draftsDao(): DraftsDao
    fun notificationRuleDao(): NotificationRuleDao
    fun notificationCategoryDao(): NotificationCategoryDao
}
