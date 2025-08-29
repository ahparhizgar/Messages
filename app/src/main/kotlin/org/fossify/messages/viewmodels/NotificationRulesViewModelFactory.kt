package org.fossify.messages.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fossify.messages.extensions.notificationCategoryDao // Assuming you'll add/have this extension
import org.fossify.messages.extensions.notificationRuleDao
import org.fossify.messages.interfaces.NotificationCategoryDao
import org.fossify.messages.interfaces.NotificationRuleDao

@Suppress("UNCHECKED_CAST")
class NotificationRulesViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationRulesViewModel::class.java)) {
            val notificationRuleDao: NotificationRuleDao = application.notificationRuleDao
            val notificationCategoryDao: NotificationCategoryDao = application.notificationCategoryDao // Added this line
            return NotificationRulesViewModel(notificationRuleDao, notificationCategoryDao) as T // Updated constructor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
