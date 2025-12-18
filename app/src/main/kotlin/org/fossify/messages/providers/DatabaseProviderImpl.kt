package org.fossify.messages.providers

import android.content.Context
import org.fossify.messages.databases.MessagesDatabase
import org.fossify.messages.interfaces.*

/**
 * Implementation of DatabaseProvider that uses the existing MessagesDatabase.
 * Maintains backward compatibility while providing a testable interface.
 */
class DatabaseProviderImpl(private val context: Context) : DatabaseProvider {
    private val database by lazy { MessagesDatabase.getInstance(context) }

    override fun conversationsDao(): ConversationsDao = database.ConversationsDao()
    override fun attachmentsDao(): AttachmentsDao = database.AttachmentsDao()
    override fun messageAttachmentsDao(): MessageAttachmentsDao = database.MessageAttachmentsDao()
    override fun messagesDao(): MessagesDao = database.MessagesDao()
    override fun draftsDao(): DraftsDao = database.DraftsDao()
    override fun notificationRuleDao(): NotificationRuleDao = database.NotificationRuleDao()
    override fun notificationCategoryDao(): NotificationCategoryDao = database.NotificationCategoryDao()
}
