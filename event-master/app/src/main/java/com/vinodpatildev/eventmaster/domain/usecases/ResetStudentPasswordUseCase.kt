package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class ResetStudentPasswordUseCase(private val repository: Repository) {
    suspend fun execute(email:String,password:String,otp:String) : Resource<Boolean>{
        return repository.resetStudentPassword(email, password, otp)
    }
}