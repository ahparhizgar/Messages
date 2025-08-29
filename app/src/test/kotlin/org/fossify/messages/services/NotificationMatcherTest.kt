package org.fossify.messages.services

import org.fossify.messages.models.MessageData
import org.fossify.messages.models.NotificationRule
import org.fossify.messages.models.NotificationRuleType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationMatcherTest {

    private val matcher = NotificationMatcher()

    private val testMessage = MessageData(sender = "1234567890", messageBody = "Hello world, this is a test message.")

    @Test
    fun `findMatchingRule returns null for empty rule list`() {
        val rule = matcher.findMatchingRule(testMessage, emptyList())
        assertNull(rule)
    }

    @Test
    fun `findMatchingRule returns null if no rule matches`() {
        val rules = listOf(
            NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "00000", categoryId = 1, order = 1),
            NotificationRule(id = 2, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "nonexistent", categoryId = 2, order = 2)
        )
        val rule = matcher.findMatchingRule(testMessage, rules)
        assertNull(rule)
    }

    @Test
    fun `findMatchingRule returns first matching rule by order`() {
        val rule1 = NotificationRule(id = 1, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "test message", categoryId = 1, order = 1)
        val rule2 = NotificationRule(id = 2, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "1234567890", categoryId = 2, order = 2)
        val rules = listOf(rule1, rule2)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(rule1, matchedRule)
    }

    @Test
    fun `findMatchingRule respects rule order even if later rule also matches`() {
        val rule1 = NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "9999999999", categoryId = 1, order = 1) // Doesn't match
        val rule2 = NotificationRule(id = 2, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "Hello world", categoryId = 2, order = 2) // Matches
        val rule3 = NotificationRule(id = 3, ruleType = NotificationRuleType.PHONE_NUMBER_REGEX, valueToMatch = "123.*", categoryId = 3, order = 3) // Also matches
        val rules = listOf(rule1, rule2, rule3)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(rule2, matchedRule)
    }

    @Test
    fun `findMatchingRule skips disabled rules`() {
        val rule1 = NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "1234567890", categoryId = 1, order = 1, isEnabled = false)
        val rule2 = NotificationRule(id = 2, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "test message", categoryId = 2, order = 2, isEnabled = true)
        val rules = listOf(rule1, rule2)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(rule2, matchedRule)
    }

    @Test
    fun `findMatchingRule returns null if all matching rules are disabled`() {
        val rules = listOf(
            NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "1234567890", categoryId = 1, order = 1, isEnabled = false)
        )
        val rule = matcher.findMatchingRule(testMessage, rules)
        assertNull(rule)
    }

    // Test cases for each NotificationRuleType
    @Test
    fun `findMatchingRule matches PHONE_NUMBER_EXACT`() {
        val matchingRule = NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "1234567890", categoryId = 1, order = 1)
        val rules = listOf(matchingRule)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(matchingRule, matchedRule)
    }

    @Test
    fun `findMatchingRule matches PHONE_NUMBER_REGEX`() {
        val matchingRule = NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_REGEX, valueToMatch = "^123.*0$", categoryId = 1, order = 1)
        val rules = listOf(matchingRule)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(matchingRule, matchedRule)
    }

    @Test
    fun `findMatchingRule matches MESSAGE_CONTAINS`() {
        val matchingRule = NotificationRule(id = 1, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "world", categoryId = 1, order = 1)
        val rules = listOf(matchingRule)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(matchingRule, matchedRule)
    }

    @Test
    fun `findMatchingRule matches MESSAGE_REGEX`() {
        val matchingRule = NotificationRule(id = 1, ruleType = NotificationRuleType.MESSAGE_REGEX, valueToMatch = ".*test.*", categoryId = 1, order = 1)
        val rules = listOf(matchingRule)
        val matchedRule = matcher.findMatchingRule(testMessage, rules)
        assertEquals(matchingRule, matchedRule)
    }
}
