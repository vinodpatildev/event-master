package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class CreateEventByAdminUseCase(private val repository: Repository) {
    suspend fun execute(cookiesData: String,adminId:String, title : String, type : String, description : String, location : String, event_link : String, start: String, end: String, department : String, organizer : String, longitude : String, latitude : String):Resource<Event> {
        return repository.CreateEventAdmin(cookiesData, adminId, title, type, description, location, event_link, start, end, department, organizer, longitude, latitude)
    }
}