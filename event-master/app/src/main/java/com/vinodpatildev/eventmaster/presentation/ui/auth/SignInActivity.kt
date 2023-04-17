package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.data.model.Admin
import com.vinodpatildev.eventmaster.databinding.ActivityForgetPasswordBinding
import com.vinodpatildev.eventmaster.databinding.ActivitySignInBinding
import com.vinodpatildev.eventmaster.presentation.ui.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            when(viewModel.user){
                "admin" ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.SignInContainer, AdminSignInFragment.newInstance())
                        .commitNow()
                }
                "student"->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.SignInContainer, StudentSignInFragment.newInstance())
                        .commitNow()
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.SignInContainer, StudentSignInFragment.newInstance())
                        .commitNow()
                }
            }
        }
    }
}