package org.fossify.messages.viewmodels

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fossify.messages.interfaces.NotificationCategoryDao
import org.fossify.messages.interfaces.NotificationRuleDao
import org.fossify.messages.models.NotificationCategory
import org.fossify.messages.models.NotificationRule
import org.fossify.messages.models.NotificationRuleType

class NotificationRulesViewModel(
    private val notificationRuleDao: NotificationRuleDao,
    private val notificationCategoryDao: NotificationCategoryDao
) : ViewModel() {

    val rules: StateFlow<List<NotificationRule>> = notificationRuleDao.getAllRules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<NotificationCategory>> = notificationCategoryDao.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPhoneNumberRegexRule(regex: String, categoryId: Long) {
        viewModelScope.launch {
            val newOrder = rules.value.size // Consider a more robust ordering mechanism
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

    @RequiresApi(Build.VERSION_CODES.O) // NotificationChannel requires API 26
    fun addNotificationCategory(channelName: String, channelDescription: String?, context: Context) {
        viewModelScope.launch {
            // Create a unique channel ID.
            val channelId = "custom_channel_${System.currentTimeMillis()}"

            // Create NotificationChannel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Save the category to the database
            val newCategory = NotificationCategory(
                name = channelName,
                channelId = channelId,
                description = channelDescription
            )
            notificationCategoryDao.insert(newCategory)
        }
    }
}
