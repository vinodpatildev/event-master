package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class UpdatePasswordStudentUseCase(private val repository: Repository) {
    suspend fun execute(cookies:String, studentId:String, oldPassword:String, newPassword:String ): Resource<Boolean> {
        return repository.updatePasswordStudent(cookies, studentId, oldPassword, newPassword)
    }
}