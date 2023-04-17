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
    fun provideViewModelFactory(
        application: Application,
        preferencesManager: PreferencesManager,
        getEventsUseCase: GetEventsUseCase,
        reloadEventsUseCase : ReloadEventsUseCase,
        getNotificationsUseCase: GetNotificationsUseCase,
        reloadNotificationsUseCase: ReloadNotificationsUseCase,
        loginStudentUseCase: SignInStudentUseCase,
        signUpStudentUseCase: SignUpStudentUseCase,
        signOutStudentUseCase: SignOutStudentUseCase,
        updateDataStudentUseCase: UpdateDataStudentUseCase,
        updatePasswordStudentUseCase: UpdatePasswordStudentUseCase,
        forgetPasswordStudentUseCase: ForgetPasswordStudentUseCase,
        updatePasswordAdminUseCase: UpdatePasswordAdminUseCase,
        resetPasswordStudentUseCase: ResetPasswordStudentUseCase,
        registerEventStudentUseCase: RegisterEventStudentUseCase,
        downloadEventCertificateStudentUseCase: DownloadEventCertificateStudentUseCase,
        getEventsRegisteredStudentUseCase: GetEventsRegisteredStudentUseCase,
        reloadEventsRegisteredStudentUseCase: ReloadEventsRegisteredStudentUseCase,
        signInAdminUseCase : SignInAdminUseCase,
        registerAdminBySuperAdminUseCase: RegisterAdminBySuperAdminUseCase,
        signOutAdminUseCase: SignOutAdminUseCase,
        getEventsCreatedAdminUseCase: GetEventsCreatedAdminUseCase,
        reloadEventsCreatedAdminUseCase: ReloadEventsCreatedAdminUseCase,
        createEventByAdminUseCase: CreateEventByAdminUseCase,
        getEventReportAdminUseCase: GetEventReportAdminUseCase,
        uploadProfilePictureUseCase: UploadProfilePictureUseCase,
        updateProfilePictureStudentUseCase:UpdateProfilePictureStudentUseCase,
        updateProfilePictureAdminUseCase:UpdateProfilePictureAdminUseCase,
        forgetPasswordAdminUseCase : ForgetPasswordAdminUseCase,
        resetPasswordAdminUseCase : ResetPasswordAdminUseCase

    ): ViewModelFactory {
        return ViewModelFactory(application,
            preferencesManager,
            getEventsUseCase,
            reloadEventsUseCase,
            getNotificationsUseCase,
            reloadNotificationsUseCase,
            loginStudentUseCase,
            signUpStudentUseCase,
            signOutStudentUseCase,
            updateDataStudentUseCase,
            updatePasswordStudentUseCase,
            updatePasswordAdminUseCase,
            forgetPasswordStudentUseCase,
            resetPasswordStudentUseCase,
            registerEventStudentUseCase,
            downloadEventCertificateStudentUseCase,
            getEventsRegisteredStudentUseCase,
            reloadEventsRegisteredStudentUseCase,
            signInAdminUseCase,
            registerAdminBySuperAdminUseCase,
            signOutAdminUseCase,
            getEventsCreatedAdminUseCase,
            reloadEventsCreatedAdminUseCase,
            createEventByAdminUseCase,
            getEventReportAdminUseCase,
            uploadProfilePictureUseCase,
            updateProfilePictureStudentUseCase,
            updateProfilePictureAdminUseCase,
            forgetPasswordAdminUseCase,
            resetPasswordAdminUseCase
        )
    }
}