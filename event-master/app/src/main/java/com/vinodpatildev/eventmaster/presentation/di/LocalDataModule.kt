package com.vinodpatildev.eventmaster.presentation.di

import com.vinodpatildev.eventmaster.data.db.EventDao
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import com.vinodpatildev.eventmaster.data.repository.datasourceimpl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Singleton
    @Provides
    fun provideNewsLocalDataSource(articleDao: EventDao):LocalDataSource{
        return LocalDataSourceImpl(articleDao)
    }
}