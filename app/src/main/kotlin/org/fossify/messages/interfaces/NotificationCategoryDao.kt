package org.fossify.messages.interfaces

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.fossify.messages.models.NotificationCategory

@Dao
interface NotificationCategoryDao {

    @Query("SELECT * FROM notification_categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<NotificationCategory>>

    @Query("SELECT * FROM notification_categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): NotificationCategory?

    @Query("SELECT * FROM notification_categories WHERE channelId = :channelId")
    suspend fun getCategoryByChannelId(channelId: String): NotificationCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: NotificationCategory): Long

    @Update
    suspend fun update(category: NotificationCategory)

    @Delete
    suspend fun delete(category: NotificationCategory)

    @Query("DELETE FROM notification_categories WHERE id = :id")
    suspend fun deleteById(id: Long)
}
