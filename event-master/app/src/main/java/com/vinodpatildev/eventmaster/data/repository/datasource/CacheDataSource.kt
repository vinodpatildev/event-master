package com.vinodpatildev.eventmaster.data.repository.datasource

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification

interface CacheDataSource {
    suspend fun getEventsFromCache() : List<Event>
    suspend fun saveEventsToCache(events:List<Event>)

    suspend fun getRegisteredEventsFromCache() : List<Event>
    suspend fun saveRegisteredEventsToCache(events:List<Event>)

    suspend fun getCreatedEventsFromCache() : List<Event>
    suspend fun saveCreatedEventsToCache(events:List<Event>)

    suspend fun getNotificationsFromCache() : List<Notification>
    suspend fun saveNotificationsToCache(notifications:List<Notification>)


}