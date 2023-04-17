package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class ForgetPasswordAdminUseCase(private val repository: Repository) {
    suspend fun execute(username:String,email:String) : Resource<Boolean> {
        return repository.forgetPasswordAdmin(username,email)
    }
}