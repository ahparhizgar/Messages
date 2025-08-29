package org.fossify.messages.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.fossify.messages.interfaces.NotificationRuleDao
import org.fossify.messages.models.NotificationRule

class NotificationRulesViewModel(private val notificationRuleDao: NotificationRuleDao) : ViewModel() {

    val rules: StateFlow<List<NotificationRule>> = notificationRuleDao.getAllRules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
