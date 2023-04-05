package com.vinodpatildev.eventmaster.presentation.di

import android.app.Application
import com.vinodpatildev.eventmaster.data.model.PreferencesManager
import com.vinodpatildev.eventmaster.domain.usecases.*
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Singleton
    @Provides
    fun provideNewsViewModelFactory(
        application: Application,
        preferencesManager: PreferencesManager,
        getEventsUseCase: GetEventsUseCase,
        getNotificationsUseCase: GetNotificationsUseCase,
        loginStudentUseCase: SignInStudentUseCase,
        signUpStudentUseCase: SignUpStudentUseCase,
        signOutStudentUseCase: SignOutStudentUseCase,
        updateStudentDataUseCase: UpdateStudentDataUseCase,
        updateStudentPasswordUseCase: UpdateStudentPasswordUseCase
    ): ViewModelFactory {
        return ViewModelFactory(application, preferencesManager ,getEventsUseCase, getNotificationsUseCase ,loginStudentUseCase, signUpStudentUseCase, signOutStudentUseCase, updateStudentDataUseCase, updateStudentPasswordUseCase)
    }
}