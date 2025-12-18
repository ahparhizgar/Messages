package org.fossify.messages.providers

import android.content.Context
import org.fossify.messages.helpers.Config
import org.fossify.messages.models.Conversation

/**
 * Implementation of PreferencesProvider that delegates to the existing Config class.
 * This maintains backward compatibility while providing a testable interface.
 */
class PreferencesProviderImpl(context: Context) : PreferencesProvider {
    private val config = Config.newInstance(context)

    override fun saveUseSIMIdAtNumber(number: String, SIMId: Int) =
        config.saveUseSIMIdAtNumber(number, SIMId)

    override fun getUseSIMIdAtNumber(number: String): Int =
        config.getUseSIMIdAtNumber(number)

    override var showCharacterCounter: Boolean
        get() = config.showCharacterCounter
        set(value) {
            config.showCharacterCounter = value
        }

    override var useSimpleCharacters: Boolean
        get() = config.useSimpleCharacters
        set(value) {
            config.useSimpleCharacters = value
        }

    override var sendOnEnter: Boolean
        get() = config.sendOnEnter
        set(value) {
            config.sendOnEnter = value
        }

    override var enableDeliveryReports: Boolean
        get() = config.enableDeliveryReports
        set(value) {
            config.enableDeliveryReports = value
        }

    override var sendLongMessageMMS: Boolean
        get() = config.sendLongMessageMMS
        set(value) {
            config.sendLongMessageMMS = value
        }

    override var sendGroupMessageMMS: Boolean
        get() = config.sendGroupMessageMMS
        set(value) {
            config.sendGroupMessageMMS = value
        }

    override var lockScreenVisibilitySetting: Int
        get() = config.lockScreenVisibilitySetting
        set(value) {
            config.lockScreenVisibilitySetting = value
        }

    override var mmsFileSizeLimit: Long
        get() = config.mmsFileSizeLimit
        set(value) {
            config.mmsFileSizeLimit = value
        }

    override var pinnedConversations: Set<String>
        get() = config.pinnedConversations
        set(value) {
            config.pinnedConversations = value
        }

    override var blockedKeywords: Set<String>
        get() = config.blockedKeywords
        set(value) {
            config.blockedKeywords = value
        }

    override var exportSms: Boolean
        get() = config.exportSms
        set(value) {
            config.exportSms = value
        }

    override var exportMms: Boolean
        get() = config.exportMms
        set(value) {
            config.exportMms = value
        }

    override var importSms: Boolean
        get() = config.importSms
        set(value) {
            config.importSms = value
        }

    override var importMms: Boolean
        get() = config.importMms
        set(value) {
            config.importMms = value
        }

    override var wasDbCleared: Boolean
        get() = config.wasDbCleared
        set(value) {
            config.wasDbCleared = value
        }

    override var keyboardHeight: Int
        get() = config.keyboardHeight
        set(value) {
            config.keyboardHeight = value
        }

    override var useRecycleBin: Boolean
        get() = config.useRecycleBin
        set(value) {
            config.useRecycleBin = value
        }

    override var lastRecycleBinCheck: Long
        get() = config.lastRecycleBinCheck
        set(value) {
            config.lastRecycleBinCheck = value
        }

    override var isArchiveAvailable: Boolean
        get() = config.isArchiveAvailable
        set(value) {
            config.isArchiveAvailable = value
        }

    override var customNotifications: Set<String>
        get() = config.customNotifications
        set(value) {
            config.customNotifications = value
        }

    override var lastBlockedKeywordExportPath: String
        get() = config.lastBlockedKeywordExportPath
        set(value) {
            config.lastBlockedKeywordExportPath = value
        }

    override fun addPinnedConversationByThreadId(threadId: Long) =
        config.addPinnedConversationByThreadId(threadId)

    override fun addPinnedConversations(conversations: List<Conversation>) =
        config.addPinnedConversations(conversations)

    override fun removePinnedConversationByThreadId(threadId: Long) =
        config.removePinnedConversationByThreadId(threadId)

    override fun removePinnedConversations(conversations: List<Conversation>) =
        config.removePinnedConversations(conversations)

    override fun addBlockedKeyword(keyword: String) =
        config.addBlockedKeyword(keyword)

    override fun removeBlockedKeyword(keyword: String) =
        config.removeBlockedKeyword(keyword)

    override fun addCustomNotificationsByThreadId(threadId: Long) =
        config.addCustomNotificationsByThreadId(threadId)

    override fun removeCustomNotificationsByThreadId(threadId: Long) =
        config.removeCustomNotificationsByThreadId(threadId)
}
