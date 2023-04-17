package com.vinodpatildev.eventmaster.data.repository.datasource

import com.vinodpatildev.eventmaster.data.model.CreatedEvent
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.RegisteredEvent
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveAllEventsToDB(events: List<Event>)

    suspend fun getAllEventsFromDB():List<Event>

    suspend fun deleteAllEventsFromDB()

    suspend fun saveAllRegisteredEventsToDB(events: List<RegisteredEvent>)

    suspend fun getAllRegisteredEventsFromDB():List<RegisteredEvent>

    suspend fun deleteAllRegisteredEventsFromDB()

    suspend fun saveAllCreatedEventsToDB(events: List<CreatedEvent>)

    suspend fun getAllCreatedEventsFromDB():List<CreatedEvent>

    suspend fun deleteAllCreatedEventsFromDB()

    suspend fun saveAllNotificationsToDB(notifications: List<Notification>)

    suspend fun getAllNotificationsFromDB():List<Notification>

    suspend fun deleteAllNotificationsFromDB()


}