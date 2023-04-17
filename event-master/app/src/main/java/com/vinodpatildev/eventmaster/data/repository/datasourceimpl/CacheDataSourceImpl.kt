package com.vinodpatildev.eventmaster.data.repository.datasourceimpl

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.repository.datasource.CacheDataSource

class CacheDataSourceImpl : CacheDataSource {
    private var eventListCache = ArrayList<Event>()
    private var registeredEventListCache = ArrayList<Event>()
    private var createdEventListCache = ArrayList<Event>()
    private var notificationListCache = ArrayList<Notification>()

    override suspend fun getEventsFromCache() : List<Event> {
        return eventListCache
    }

    override suspend fun saveEventsToCache(events: List<Event>) {
        eventListCache.clear()
        eventListCache = ArrayList(events)
    }

    override suspend fun getRegisteredEventsFromCache(): List<Event> {
        return registeredEventListCache
    }

    override suspend fun saveRegisteredEventsToCache(events: List<Event>) {
        registeredEventListCache.clear()
        registeredEventListCache = ArrayList(events)
    }

    override suspend fun getCreatedEventsFromCache(): List<Event> {
        return createdEventListCache
    }

    override suspend fun saveCreatedEventsToCache(events: List<Event>) {
        createdEventListCache
        createdEventListCache = ArrayList(events)
    }

    override suspend fun getNotificationsFromCache(): List<Notification> {
        return notificationListCache
    }

    override suspend fun saveNotificationsToCache(notifications: List<Notification>) {
        notificationListCache.clear()
        notificationListCache = ArrayList(notifications)
    }
}