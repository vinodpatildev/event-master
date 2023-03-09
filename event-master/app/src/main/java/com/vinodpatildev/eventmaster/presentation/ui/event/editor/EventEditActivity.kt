package com.vinodpatildev.eventmaster.presentation.ui.event.editor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinodpatildev.eventmaster.databinding.ActivityEventEditBinding

class EventEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}