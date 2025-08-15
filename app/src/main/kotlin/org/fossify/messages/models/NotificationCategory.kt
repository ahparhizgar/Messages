package org.fossify.messages.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notification_categories", indices = [Index(value = ["channelId"], unique = true)])
data class NotificationCategory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val channelId: String,
    val description: String? = null
)
