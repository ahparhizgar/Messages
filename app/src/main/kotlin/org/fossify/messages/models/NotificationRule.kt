package org.fossify.messages.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.fossify.messages.services.MessageContainsMatcher
import org.fossify.messages.services.MessageRegexMatcher
import org.fossify.messages.services.PhoneNumberExactMatcher
import org.fossify.messages.services.PhoneNumberRegexMatcher
import org.fossify.messages.services.RuleMatcher

data class MessageData(
    val sender: String,
    val messageBody: String, // Add other relevant fields like SIM slot, etc. if needed
)

@Entity(
    tableName = "notification_rules",
    foreignKeys = [ForeignKey(
        entity = NotificationCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class NotificationRule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleType: NotificationRuleType,
    val valueToMatch: String,
    val categoryId: Long,
    val order: Int,
    val isEnabled: Boolean = true
) {
    fun matches(message: MessageData): Boolean {
        if (!isEnabled) {
            return false
        }
        val matcher: RuleMatcher = when (ruleType) {
            NotificationRuleType.PHONE_NUMBER_EXACT -> PhoneNumberExactMatcher(valueToMatch)
            NotificationRuleType.PHONE_NUMBER_REGEX -> PhoneNumberRegexMatcher(valueToMatch)
            NotificationRuleType.MESSAGE_CONTAINS -> MessageContainsMatcher(valueToMatch)
            NotificationRuleType.MESSAGE_REGEX -> MessageRegexMatcher(valueToMatch)
        }
        return matcher.matches(message)
    }
}
