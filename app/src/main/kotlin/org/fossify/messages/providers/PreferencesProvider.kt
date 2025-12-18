package org.fossify.messages.providers

import org.fossify.messages.models.Conversation

/**
 * Interface for managing application preferences.
 * This abstraction allows for easy mocking in tests and decouples from Android SharedPreferences.
 */
interface PreferencesProvider {
    fun saveUseSIMIdAtNumber(number: String, SIMId: Int)
    fun getUseSIMIdAtNumber(number: String): Int

    var showCharacterCounter: Boolean
    var useSimpleCharacters: Boolean
    var sendOnEnter: Boolean
    var enableDeliveryReports: Boolean
    var sendLongMessageMMS: Boolean
    var sendGroupMessageMMS: Boolean
    var lockScreenVisibilitySetting: Int
    var mmsFileSizeLimit: Long
    var pinnedConversations: Set<String>
    var blockedKeywords: Set<String>
    var exportSms: Boolean
    var exportMms: Boolean
    var importSms: Boolean
    var importMms: Boolean
    var wasDbCleared: Boolean
    var keyboardHeight: Int
    var useRecycleBin: Boolean
    var lastRecycleBinCheck: Long
    var isArchiveAvailable: Boolean
    var customNotifications: Set<String>
    var lastBlockedKeywordExportPath: String

    fun addPinnedConversationByThreadId(threadId: Long)
    fun addPinnedConversations(conversations: List<Conversation>)
    fun removePinnedConversationByThreadId(threadId: Long)
    fun removePinnedConversations(conversations: List<Conversation>)
    fun addBlockedKeyword(keyword: String)
    fun removeBlockedKeyword(keyword: String)
    fun addCustomNotificationsByThreadId(threadId: Long)
    fun removeCustomNotificationsByThreadId(threadId: Long)
}
