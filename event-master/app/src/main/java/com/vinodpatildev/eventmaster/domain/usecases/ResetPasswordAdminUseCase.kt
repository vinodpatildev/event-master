package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class ResetPasswordAdminUseCase(private val repository: Repository) {
    suspend fun execute(email:String,password:String,otp:String) : Resource<Boolean>{
        return repository.resetPasswordStudent(email, password, otp)
    }
}