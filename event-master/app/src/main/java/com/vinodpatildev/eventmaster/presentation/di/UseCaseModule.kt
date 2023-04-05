package com.vinodpatildev.eventmaster.presentation.di

import com.vinodpatildev.eventmaster.domain.repository.Repository
import com.vinodpatildev.eventmaster.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideEventsUseCase(repository: Repository) : GetEventsUseCase {
        return GetEventsUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideNotificationsUseCase(repository: Repository) : GetNotificationsUseCase {
        return GetNotificationsUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideSignInStudentUseCase(repository: Repository) : SignInStudentUseCase {
        return SignInStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignOutStudentUseCase(repository: Repository) : SignOutStudentUseCase {
        return SignOutStudentUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideSignUpStudentUseCase(repository: Repository) : SignUpStudentUseCase {
        return SignUpStudentUseCase(repository)
    }


    @Singleton
    @Provides
    fun provideUpdateStudentDataUseCase(repository: Repository) : UpdateStudentDataUseCase {
        return UpdateStudentDataUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateStudentPasswordUseCase(repository: Repository) : UpdateStudentPasswordUseCase {
        return UpdateStudentPasswordUseCase(repository)
    }
}