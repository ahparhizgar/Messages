package org.fossify.messages.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fossify.messages.interfaces.NotificationRuleDao
import org.fossify.messages.models.NotificationRule
import org.fossify.messages.models.NotificationRuleType

class NotificationRulesViewModel(private val notificationRuleDao: NotificationRuleDao) : ViewModel() {

    val rules: StateFlow<List<NotificationRule>> = notificationRuleDao.getAllRules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPhoneNumberRegexRule(regex: String, categoryId: Long) {
        viewModelScope.launch {
            // A simple way to determine order; consider a more robust approach for production
            // This gets the order based on all rules, might need to be specific to the category
            val newOrder = rules.value.size 

            val newRule = NotificationRule(
                ruleType = NotificationRuleType.PHONE_NUMBER_REGEX,
                valueToMatch = regex,
                categoryId = categoryId,
                order = newOrder,
                isEnabled = true
            )
            notificationRuleDao.insert(newRule)
        }
    }
}
