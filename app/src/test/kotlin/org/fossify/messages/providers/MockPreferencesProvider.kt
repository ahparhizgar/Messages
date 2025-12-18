package org.fossify.messages.providers

import org.fossify.messages.models.Conversation

/**
 * Mock implementation of PreferencesProvider for testing.
 * This allows tests to run without Android dependencies.
 */
class MockPreferencesProvider : PreferencesProvider {
    private val storage = mutableMapOf<String, Any>()
    
    override fun saveUseSIMIdAtNumber(number: String, SIMId: Int) {
        storage["sim_$number"] = SIMId
    }

    override fun getUseSIMIdAtNumber(number: String): Int =
        storage["sim_$number"] as? Int ?: 0

    override var showCharacterCounter: Boolean = false
    override var useSimpleCharacters: Boolean = false
    override var sendOnEnter: Boolean = false
    override var enableDeliveryReports: Boolean = false
    override var sendLongMessageMMS: Boolean = false
    override var sendGroupMessageMMS: Boolean = false
    override var lockScreenVisibilitySetting: Int = 0
    override var mmsFileSizeLimit: Long = 0L
    override var pinnedConversations: Set<String> = emptySet()
    override var blockedKeywords: Set<String> = emptySet()
    override var exportSms: Boolean = true
    override var exportMms: Boolean = true
    override var importSms: Boolean = true
    override var importMms: Boolean = true
    override var wasDbCleared: Boolean = false
    override var keyboardHeight: Int = 0
    override var useRecycleBin: Boolean = false
    override var lastRecycleBinCheck: Long = 0L
    override var isArchiveAvailable: Boolean = true
    override var customNotifications: Set<String> = emptySet()
    override var lastBlockedKeywordExportPath: String = ""

    override fun addPinnedConversationByThreadId(threadId: Long) {
        pinnedConversations = pinnedConversations.plus(threadId.toString())
    }

    override fun addPinnedConversations(conversations: List<Conversation>) {
        pinnedConversations = pinnedConversations.plus(conversations.map { it.threadId.toString() })
    }

    override fun removePinnedConversationByThreadId(threadId: Long) {
        pinnedConversations = pinnedConversations.minus(threadId.toString())
    }

    override fun removePinnedConversations(conversations: List<Conversation>) {
        pinnedConversations = pinnedConversations.minus(conversations.map { it.threadId.toString() })
    }

    override fun addBlockedKeyword(keyword: String) {
        blockedKeywords = blockedKeywords.plus(keyword)
    }

    override fun removeBlockedKeyword(keyword: String) {
        blockedKeywords = blockedKeywords.minus(keyword)
    }

    override fun addCustomNotificationsByThreadId(threadId: Long) {
        customNotifications = customNotifications.plus(threadId.toString())
    }

    override fun removeCustomNotificationsByThreadId(threadId: Long) {
        customNotifications = customNotifications.minus(threadId.toString())
    }
}
