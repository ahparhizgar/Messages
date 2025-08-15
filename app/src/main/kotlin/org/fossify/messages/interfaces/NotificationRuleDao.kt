package org.fossify.messages.interfaces

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.fossify.messages.models.NotificationRule

@Dao
interface NotificationRuleDao {

    @Query("SELECT * FROM notification_rules ORDER BY `order` ASC")
    fun getAllRules(): Flow<List<NotificationRule>>

    @Query("SELECT * FROM notification_rules WHERE categoryId = :categoryId ORDER BY `order` ASC")
    fun getRulesForCategory(categoryId: Long): Flow<List<NotificationRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: NotificationRule): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules: List<NotificationRule>)

    @Update
    suspend fun update(rule: NotificationRule)

    @Delete
    suspend fun delete(rule: NotificationRule)

    @Query("DELETE FROM notification_rules WHERE id = :id")
    suspend fun deleteById(id: Long)
}
