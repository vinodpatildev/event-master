package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class MarkAttendanceEventStudentUseCase(private val repository: Repository) {
    suspend fun execute(cookies:String, studentId:String, eventId:String): Resource<Boolean> {
        return repository.markAttendanceForEventStudent(cookies, studentId, eventId)
    }
}