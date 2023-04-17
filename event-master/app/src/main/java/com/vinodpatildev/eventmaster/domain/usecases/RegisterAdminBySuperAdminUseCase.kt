package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class RegisterAdminBySuperAdminUseCase(private val repository: Repository) {
    suspend fun execute(cookiesData:String, username: String, email: String, password: String, name:String): Resource<Boolean> {
        return repository.registerAdminBySuperAdmin(cookiesData,username, email, password, name)
    }
}