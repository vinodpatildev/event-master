package com.vinodpatildev.eventmaster.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vinodpatildev.eventmaster.data.repository.auth.LoginDataSource
import com.vinodpatildev.eventmaster.data.repository.auth.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource( FirebaseAuth.getInstance() )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}