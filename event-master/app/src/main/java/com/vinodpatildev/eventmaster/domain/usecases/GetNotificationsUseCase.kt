package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class GetNotificationsUseCase(private val repository: Repository) {
    suspend fun execute(cookiesData:String): Resource<List<Notification>> {
        return repository.getNotifications(cookiesData)
    }
}
