package com.vinodpatildev.eventmaster.data.repository.datasource

import com.vinodpatildev.eventmaster.data.model.Event
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveArticleToDB(article: Event)

    suspend fun deleteArticleFromDB(article: Event)

    fun getAllArticlesFromDB(): Flow<List<Event>>
}