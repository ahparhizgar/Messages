package org.fossify.messages.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_rules",
    foreignKeys = [ForeignKey(
        entity = NotificationCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class NotificationRule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleType: NotificationRuleType,
    val valueToMatch: String,
    val categoryId: Long,
    val order: Int,
    val isEnabled: Boolean = true
)
