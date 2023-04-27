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
    fun provideUpdateDataStudentUseCase(repository: Repository) : UpdateDataStudentUseCase {
        return UpdateDataStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdatePasswordStudentUseCase(repository: Repository) : UpdatePasswordStudentUseCase {
        return UpdatePasswordStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideForgetPasswordStudentUseCase(repository:Repository) : ForgetPasswordStudentUseCase {
        return ForgetPasswordStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideResetPasswordStudentUseCase(repository:Repository) : ResetPasswordStudentUseCase {
        return ResetPasswordStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideRegisterEventStudentUseCase(repository: Repository) : RegisterEventStudentUseCase {
        return RegisterEventStudentUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideMarkAttendanceEventStudentUseCase(repository: Repository) : MarkAttendanceEventStudentUseCase {
        return MarkAttendanceEventStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDownloadEventCertificateStudentUseCase(repository: Repository) : DownloadEventCertificateStudentUseCase {
        return DownloadEventCertificateStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetEventsRegisteredStuentUseCase(repository: Repository) : GetEventsRegisteredStudentUseCase {
        return GetEventsRegisteredStudentUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignInAdminUseCase(repository: Repository) : SignInAdminUseCase {
        return SignInAdminUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideRegisterAdminBySuperAdminUseCase(repository: Repository) : RegisterAdminBySuperAdminUseCase {
        return RegisterAdminBySuperAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignOutAdminUseCase(repository: Repository) : SignOutAdminUseCase {
        return SignOutAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideEventsCreatedAdminUseCase(repository: Repository) : GetEventsCreatedAdminUseCase {
        return GetEventsCreatedAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCreateEventByAdminUseCase(repository: Repository) : CreateEventByAdminUseCase {
        return CreateEventByAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetEventReportAdminUseCase(repository: Repository) : GetEventReportAdminUseCase {
        return GetEventReportAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideReloadEventsUseCase(repository: Repository) : ReloadEventsUseCase {
        return ReloadEventsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideReloadNotificationsUseCase(repository: Repository) : ReloadNotificationsUseCase {
        return ReloadNotificationsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideReloadEventsRegisteredStudentUseCase(repository: Repository) : ReloadEventsRegisteredStudentUseCase {
        return ReloadEventsRegisteredStudentUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideReloadEventsCreatedAdminUseCase(repository: Repository) : ReloadEventsCreatedAdminUseCase {
        return ReloadEventsCreatedAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdatePasswordAdminUseCase(repository: Repository) : UpdatePasswordAdminUseCase {
        return UpdatePasswordAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUploadProfilePictureUseCase(repository: Repository) : UploadProfilePictureUseCase {
        return UploadProfilePictureUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateProfilePictureStudentUseCase(repository: Repository) : UpdateProfilePictureStudentUseCase {
        return UpdateProfilePictureStudentUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideUpdateProfilePictureAdminUseCase(repository: Repository) : UpdateProfilePictureAdminUseCase {
        return UpdateProfilePictureAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideForgetPasswordAdminUseCase(repository: Repository) : ForgetPasswordAdminUseCase {
        return ForgetPasswordAdminUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideResetPasswordAdminUseCase(repository: Repository) : ResetPasswordAdminUseCase {
        return ResetPasswordAdminUseCase(repository)
    }

}

