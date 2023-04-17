package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class GetEventReportAdminUseCase(private val repository: Repository) {
    suspend fun execute(cookiesData: String,eventId:String ): Resource<List<Student>> {
        return repository.getEventReportAdmin(cookiesData,eventId)
    }
}