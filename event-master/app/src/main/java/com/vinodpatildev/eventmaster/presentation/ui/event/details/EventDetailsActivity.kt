package com.vinodpatildev.eventmaster.presentation.ui.event.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
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
        val imageArray = intArrayOf(
            R.drawable.placeholder_image1,
            R.drawable.placeholder_image2,
            R.drawable.placeholder_image3,
            R.drawable.placeholder_image4,
            R.drawable.placeholder_image5,
            R.drawable.placeholder_image6,
            R.drawable.placeholder_image7,
            R.drawable.placeholder_image8,
            R.drawable.placeholder_image9,
        )
        val clickedEvent: Event = intent.getSerializableExtra(Event.TAG) as Event

        binding.ivEventPicture.setImageDrawable(ContextCompat.getDrawable(this,imageArray[(Math.abs(clickedEvent._id.hashCode()))%imageArray.size]))
        binding.tvEventFieldIdValue.text = clickedEvent._id
        binding.tvEventFieldStatusValue.text = clickedEvent.state
        binding.tvEventFieldTitleValue.text = clickedEvent.title
        binding.tvEventFieldTypeValue.text = clickedEvent.type
        binding.tvEventFieldDepartmentValue.text = clickedEvent.department
        binding.tvEventFieldOrganiserValue.text = clickedEvent.organizer
        binding.tvEventFielStartTimeValue.text = clickedEvent.start
        binding.tvEventFieldEndTimeValue.text = clickedEvent.end
        binding.tvEventFieldDescriptionValue.text = clickedEvent.description
    }
}