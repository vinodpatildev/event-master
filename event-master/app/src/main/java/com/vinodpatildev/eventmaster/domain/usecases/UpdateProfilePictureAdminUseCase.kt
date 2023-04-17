package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class UpdateProfilePictureAdminUseCase(private val repository:Repository) {
    suspend fun execute( cookiesData: String, adminId: String, url: String ): Resource<String> {
        return repository.updateProfilePictureAdmin(cookiesData, adminId, url)
    }
}