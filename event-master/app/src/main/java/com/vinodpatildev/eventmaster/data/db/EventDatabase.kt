package com.vinodpatildev.eventmaster.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinodpatildev.eventmaster.data.model.Event

@Database(
    entities = [Event::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun getArticleDao():EventDao
}