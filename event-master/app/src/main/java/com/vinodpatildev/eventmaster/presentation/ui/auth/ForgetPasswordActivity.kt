package com.vinodpatildev.eventmaster.presentation.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinodpatildev.eventmaster.R

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        supportActionBar?.hide()
    }
}