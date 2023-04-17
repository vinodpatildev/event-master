package com.vinodpatildev.eventmaster.domain.usecases

import android.net.Uri
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository
import java.io.File

class UploadProfilePictureUseCase(private val repository: Repository) {
    suspend fun execute(userId: String, imageUri: Uri): Resource<String> {
        return repository.uploadProfilePicture(userId, imageUri)
    }
}