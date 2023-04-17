package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class UpdateProfilePictureStudentUseCase(private val repository: Repository) {
    suspend fun execute( cookiesData: String, studentId: String, url: String ): Resource<String> {
        return repository.updateProfilePictureStudent(cookiesData, studentId, url)
    }
}