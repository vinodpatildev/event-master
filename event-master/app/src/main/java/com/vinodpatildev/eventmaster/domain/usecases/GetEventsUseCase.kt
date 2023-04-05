package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class GetEventsUseCase(private val repository: Repository) {
    suspend fun execute(cookiesData:String): Resource<List<Event>> {
        return repository.getEvents(cookiesData)
    }
}