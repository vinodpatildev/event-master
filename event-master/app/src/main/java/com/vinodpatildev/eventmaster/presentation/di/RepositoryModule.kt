package com.vinodpatildev.eventmaster.presentation.di

import com.vinodpatildev.eventmaster.data.repository.RepositoryImpl
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(remoteDataSource: RemoteDataSource, newsLocalDataSource: LocalDataSource):Repository{
        return RepositoryImpl(remoteDataSource, newsLocalDataSource)
    }
}