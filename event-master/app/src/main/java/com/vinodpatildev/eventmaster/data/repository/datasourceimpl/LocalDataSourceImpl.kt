package com.vinodpatildev.eventmaster.data.repository.datasourceimpl

import android.util.Log
import com.vinodpatildev.eventmaster.data.db.EventDao
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val articleDao: EventDao):LocalDataSource {
    override suspend fun saveArticleToDB(article: Event) {
        return articleDao.insert(article)
    }

    override suspend fun deleteArticleFromDB(article: Event) {
        articleDao.delete(article)
    }

    override fun getAllArticlesFromDB(): Flow<List<Event>> {
        Log.i("logcat","data observed from : LocalDataSourceImpl start")
        return articleDao.getAllArticles()
    }

}