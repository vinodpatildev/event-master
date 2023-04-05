package com.vinodpatildev.eventmaster.presentation.di

import com.vinodpatildev.eventmaster.data.api.ApiService
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.data.repository.datasourceimpl.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: ApiService):RemoteDataSource{
        return RemoteDataSourceImpl(apiService)
    }
}