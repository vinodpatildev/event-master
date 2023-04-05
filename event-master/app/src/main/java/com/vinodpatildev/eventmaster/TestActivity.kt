package com.vinodpatildev.eventmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    var tvResult : TextView? = null
    var btnRun : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        tvResult = findViewById(R.id.tv_result);
        btnRun = findViewById(R.id.btnGetApiResult);


        btnRun?.setOnClickListener {

        }

    }
}