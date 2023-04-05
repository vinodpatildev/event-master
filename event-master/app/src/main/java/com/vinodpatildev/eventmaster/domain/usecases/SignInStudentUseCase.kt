package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.ApiResponse
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository


class SignInStudentUseCase(private val repository: Repository) {
    suspend fun execute( username: String, password: String): Resource<ApiResponse<Student>> {
        return repository.signInStudent(username,password)
    }
}