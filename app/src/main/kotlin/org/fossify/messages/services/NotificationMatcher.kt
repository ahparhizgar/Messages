package org.fossify.messages.services

import org.fossify.messages.models.MessageData
import org.fossify.messages.models.NotificationRule
import org.fossify.messages.models.matches

/**
 * Processes a list of notification rules against a message to find a match.
 * Rules are processed in their given order; the first rule that matches is returned.
 * Disabled rules are skipped.
 */
class NotificationMatcher {

    /**
     * Finds the first enabled notification rule that matches the given message.
     *
     * @param message The message data to check against the rules.
     * @param rules The list of notification rules, expected to be pre-sorted by desired order.
     * @return The matching NotificationRule, or null if no rule matches.
     */
    fun findMatchingRule(
        message: MessageData,
        rules: List<NotificationRule>
    ): NotificationRule? {
        for (rule in rules) {
            if (rule.isEnabled && rule.matches(message)) {
                return rule
            }
        }
        return null
    }
}
