package com.vinodpatildev.eventmaster.presentation.di

import android.app.Application
import androidx.room.Room
import com.vinodpatildev.eventmaster.data.db.EventDao
import com.vinodpatildev.eventmaster.data.db.EventMasterDatabase
import com.vinodpatildev.eventmaster.data.db.NotificationDao
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
    fun provideEventMasterDatabase(app: Application):EventMasterDatabase{
        return Room.databaseBuilder(
            app,
            EventMasterDatabase::class.java,
            "event_manager_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEventDao(eventMasterDb:EventMasterDatabase): EventDao {
        return eventMasterDb.getEventDao()
    }

    @Singleton
    @Provides
    fun provideNotificationDao(eventMasterDb:EventMasterDatabase): NotificationDao {
        return eventMasterDb.getNotificationDao()
    }
}