package com.vinodpatildev.eventmaster.presentation.di

import android.content.Context
import com.vinodpatildev.eventmaster.data.repository.RepositoryImpl
import com.vinodpatildev.eventmaster.data.repository.datasource.CacheDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(@ApplicationContext appContext: Context,   remoteDataSource: RemoteDataSource, LocalDataSource: LocalDataSource, cacheDataSource: CacheDataSource):Repository{
        return RepositoryImpl(appContext,remoteDataSource, LocalDataSource, cacheDataSource)
    }
}