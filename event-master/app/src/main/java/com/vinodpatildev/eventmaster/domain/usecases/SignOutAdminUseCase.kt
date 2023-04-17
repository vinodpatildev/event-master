package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class SignOutAdminUseCase(private val repository: Repository) {
    suspend fun execute(): Resource<Boolean> {
        return repository.singOutAdmin()
    }
}