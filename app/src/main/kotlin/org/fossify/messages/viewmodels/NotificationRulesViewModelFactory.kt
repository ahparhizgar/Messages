package org.fossify.messages.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fossify.messages.databases.MessagesDatabase
import org.fossify.messages.extensions.notificationRuleDao
import org.fossify.messages.interfaces.NotificationRuleDao

@Suppress("UNCHECKED_CAST")
class NotificationRulesViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationRulesViewModel::class.java)) {
            val notificationRuleDao: NotificationRuleDao = application.notificationRuleDao
            return NotificationRulesViewModel(notificationRuleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
