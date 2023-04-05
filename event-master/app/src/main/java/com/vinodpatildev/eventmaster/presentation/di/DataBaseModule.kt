package com.vinodpatildev.eventmaster.presentation.di

import android.app.Application
import androidx.room.Room
import com.vinodpatildev.eventmaster.data.db.EventDao
import com.vinodpatildev.eventmaster.data.db.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideArticleDatabase(app: Application):EventDatabase{
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            "article_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(articleDb:EventDatabase): EventDao {
        return articleDb.getArticleDao()
    }
}