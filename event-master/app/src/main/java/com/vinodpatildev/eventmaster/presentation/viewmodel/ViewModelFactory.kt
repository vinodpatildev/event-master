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
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val loginStudentUseCase: SignInStudentUseCase,
    private val signUpStudentUseCase: SignUpStudentUseCase,
    private val signOutStudentUseCase: SignOutStudentUseCase,
    private val updateStudentDataUseCase: UpdateStudentDataUseCase,
    private val updateStudentPasswordUseCase: UpdateStudentPasswordUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        return ViewModel(app, preferencesManager, getEventsUseCase,getNotificationsUseCase, loginStudentUseCase, signUpStudentUseCase, signOutStudentUseCase, updateStudentDataUseCase,updateStudentPasswordUseCase ) as T
    }
}