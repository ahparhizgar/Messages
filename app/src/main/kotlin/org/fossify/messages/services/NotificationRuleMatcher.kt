package org.fossify.messages.services

import org.fossify.messages.models.MessageData

interface RuleMatcher {
    fun matches(message: MessageData): Boolean
}

class PhoneNumberExactMatcher(private val valueToMatch: String) : RuleMatcher {
    override fun matches(message: MessageData): Boolean {
        return message.sender == valueToMatch
    }
}

class PhoneNumberRegexMatcher(private val valueToMatch: String) : RuleMatcher {
    override fun matches(message: MessageData): Boolean {
        return message.sender.matches(valueToMatch.toRegex())
    }
}

class MessageContainsMatcher(private val valueToMatch: String) : RuleMatcher {
    override fun matches(message: MessageData): Boolean {
        return message.messageBody.contains(valueToMatch, ignoreCase = true)
    }
}

class MessageRegexMatcher(private val valueToMatch: String) : RuleMatcher {
    override fun matches(message: MessageData): Boolean {
        return message.messageBody.matches(valueToMatch.toRegex())
    }
}
