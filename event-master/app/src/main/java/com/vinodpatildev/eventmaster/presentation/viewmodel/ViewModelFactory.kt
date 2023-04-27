package com.vinodpatildev.eventmaster.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinodpatildev.eventmaster.data.model.PreferencesManager
import com.vinodpatildev.eventmaster.domain.usecases.*

class ViewModelFactory(
    private val app: Application,
    private val preferencesManager: PreferencesManager,
    private val getEventsUseCase: GetEventsUseCase,
    private val reloadEventsUseCase : ReloadEventsUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val reloadNotificationsUseCase: ReloadNotificationsUseCase,
    private val loginStudentUseCase: SignInStudentUseCase,
    private val signUpStudentUseCase: SignUpStudentUseCase,
    private val signOutStudentUseCase: SignOutStudentUseCase,
    private val updateDataStudentUseCase: UpdateDataStudentUseCase,
    private val updatePasswordStudentUseCase: UpdatePasswordStudentUseCase,
    private val updatePasswordAdminUseCase: UpdatePasswordAdminUseCase,
    private val forgetPasswordStudentUseCase: ForgetPasswordStudentUseCase,
    private val resetPasswordStudentUseCase: ResetPasswordStudentUseCase,
    private val registerEventStudentUseCase: RegisterEventStudentUseCase,
    private val markAttendanceEventStudentUseCase : MarkAttendanceEventStudentUseCase,
    private val downloadEventCertificateStudentUseCase: DownloadEventCertificateStudentUseCase,
    private val getEventsRegisteredStudentUseCase: GetEventsRegisteredStudentUseCase,
    private val reloadEventsRegisteredStudentUseCase: ReloadEventsRegisteredStudentUseCase,
    private val signInAdminUseCase : SignInAdminUseCase,
    private val registerAdminBySuperAdminUseCase: RegisterAdminBySuperAdminUseCase,
    private val signOutAdminUseCase: SignOutAdminUseCase,
    private val getEventsCreatedAdminUseCase: GetEventsCreatedAdminUseCase,
    private val reloadEventsCreatedAdminUseCase: ReloadEventsCreatedAdminUseCase,
    private val createEventByAdminUseCase: CreateEventByAdminUseCase,
    private val getEventReportAdminUseCase: GetEventReportAdminUseCase,
    private val uploadProfilePictureUseCase:UploadProfilePictureUseCase,
    private val updateProfilePictureStudentUseCase:UpdateProfilePictureStudentUseCase,
    private val updateProfilePictureAdminUseCase:UpdateProfilePictureAdminUseCase,
    private val forgetPasswordAdminUseCase : ForgetPasswordAdminUseCase,
    private val resetPasswordAdminUseCase : ResetPasswordAdminUseCase

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        return ViewModel(app,
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
            markAttendanceEventStudentUseCase,
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
        ) as T
    }
}