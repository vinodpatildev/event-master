package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class UpdatePasswordAdminUseCase(private val repository: Repository) {
    suspend fun execute(cookies:String, adminId:String, oldPassword:String, newPassword:String ): Resource<Boolean> {
        return repository.updatePasswordAdmin(cookies, adminId, oldPassword, newPassword)
    }
}