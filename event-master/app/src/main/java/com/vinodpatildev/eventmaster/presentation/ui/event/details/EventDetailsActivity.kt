package com.vinodpatildev.eventmaster.presentation.ui.event.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.databinding.ActivityEventDetailsBinding

class EventDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val clickedEvent: Event = intent.getSerializableExtra(Event.TAG) as Event
        binding.tvEventFieldNameValue.text = clickedEvent.eventName
        binding.tvEventFieldDateValue.text = clickedEvent.eventDate
        binding.tvEventFieldOrganizerDepartmentValue.text = clickedEvent.eventOrganizerDepartment
        binding.tvEventFieldOrganizerValue.text = clickedEvent.eventOrganizer
        binding.tvEventFieldDetailsValue.text = clickedEvent.eventDetails
    }
}