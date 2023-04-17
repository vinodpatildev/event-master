package com.vinodpatildev.eventmaster.domain.usecases

import android.content.Context
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class DownloadEventCertificateStudentUseCase(private val repository: Repository) {
    suspend fun execute(context: Context, cookies:String, studentId:String, eventId:String) : Resource<String> {
        return repository.downloadEventCertificateStudent(context, cookies, studentId, eventId)
    }
}