package com.vinodpatildev.eventmaster.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinodpatildev.eventmaster.data.model.CreatedEvent
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.RegisteredEvent

@Database(
    entities = [Event::class, RegisteredEvent::class, CreatedEvent::class, Notification::class],
    version = 1,
    exportSchema = false
)
abstract class EventMasterDatabase : RoomDatabase() {
    abstract fun getEventDao():EventDao
    abstract fun getNotificationDao():NotificationDao
}