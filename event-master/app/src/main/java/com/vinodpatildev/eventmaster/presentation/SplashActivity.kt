package com.vinodpatildev.eventmaster.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity

class SplashActivity : AppCompatActivity() {
    private var TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        },TIME_OUT)
    }
}