package com.vinodpatildev.eventmaster.data.repository.datasourceimpl

import android.util.Log
import com.vinodpatildev.eventmaster.data.db.EventDao
import com.vinodpatildev.eventmaster.data.db.NotificationDao
import com.vinodpatildev.eventmaster.data.model.CreatedEvent
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.RegisteredEvent
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocalDataSourceImpl(
    private val eventDao: EventDao,
    private val notificationDao : NotificationDao

    ):LocalDataSource {
    override suspend fun saveAllEventsToDB(events: List<Event>) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.saveEvents(events)
        }
    }

    override suspend fun getAllEventsFromDB(): List<Event> {
        return eventDao.getAllEvents()
    }

    override suspend fun deleteAllEventsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.deleteAllEvents()
        }
    }

    override suspend fun saveAllRegisteredEventsToDB(events: List<RegisteredEvent>) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.saveRegisteredEvents(events)
        }
    }

    override suspend fun getAllRegisteredEventsFromDB(): List<RegisteredEvent> {
        return eventDao.getAllRegisteredEvents()
    }

    override suspend fun deleteAllRegisteredEventsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.deleteAllRegisteredEvents()
        }
    }

    override suspend fun saveAllCreatedEventsToDB(events: List<CreatedEvent>) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.saveCreatedEvents(events)
        }
    }

    override suspend fun getAllCreatedEventsFromDB(): List<CreatedEvent> {
        return eventDao.getAllCreatedEvents()
    }

    override suspend fun deleteAllCreatedEventsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.deleteAllCreatedEvents()
        }
    }

    override suspend fun saveAllNotificationsToDB(notifications: List<Notification>) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.saveNotifications(notifications)
        }
    }

    override suspend fun getAllNotificationsFromDB(): List<Notification> {
        return notificationDao.getAllNotifications()
    }

    override suspend fun deleteAllNotificationsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.deleteAllNotifications()
        }
    }

}