package com.vinodpatildev.eventmaster.data.db

import androidx.room.*
import com.vinodpatildev.eventmaster.data.model.CreatedEvent
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.RegisteredEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvents(events: List<Event>)

    @Query("SELECT * FROM events")
    suspend fun getAllEvents() : List<Event>

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRegisteredEvents(events: List<RegisteredEvent>)

    @Query("SELECT * FROM registered_events")
    suspend fun getAllRegisteredEvents() : List<RegisteredEvent>

    @Query("DELETE FROM registered_events")
    suspend fun deleteAllRegisteredEvents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCreatedEvents(events: List<CreatedEvent>)

    @Query("SELECT * FROM created_events")
    suspend fun getAllCreatedEvents() : List<CreatedEvent>

    @Query("DELETE FROM created_events")
    suspend fun deleteAllCreatedEvents()



}