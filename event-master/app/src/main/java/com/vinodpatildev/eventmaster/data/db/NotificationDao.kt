package com.vinodpatildev.eventmaster.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotifications(events: List<Notification>)

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications() : List<Notification>

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
}