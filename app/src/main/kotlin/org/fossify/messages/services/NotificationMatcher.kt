package org.fossify.messages.services

import org.fossify.messages.models.MessageData
import org.fossify.messages.models.NotificationRule

class NotificationMatcher {

    fun findMatchingRule(message: MessageData, rules: List<NotificationRule>): NotificationRule? {
        return rules.firstOrNull { it.matches(message) }
    }
}
