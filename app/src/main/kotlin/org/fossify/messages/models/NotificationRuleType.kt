package org.fossify.messages.models

// RuleMatcher and its implementations are now in the services package
// No direct matcher instance here anymore

enum class NotificationRuleType {
    PHONE_NUMBER_REGEX,
    PHONE_NUMBER_EXACT,
    MESSAGE_CONTAINS,
    MESSAGE_REGEX,
}
