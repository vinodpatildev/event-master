package com.vinodpatildev.eventmaster.presentation.ui.event.details

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityEventDetailsBinding
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class EventDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityEventDetailsBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering event....")
        progressDialog.setCancelable(false)

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

        binding.apply {
            ivEventPicture.setImageDrawable(ContextCompat.getDrawable(this@EventDetailsActivity,imageArray[(Math.abs(clickedEvent._id.hashCode()))%imageArray.size]))
            tvEventFieldIdValue.text = clickedEvent._id
            tvEventFieldStatusValue.text = clickedEvent.state
            tvEventFieldTitleValue.text = clickedEvent.title
            tvEventFieldTypeValue.text = clickedEvent.type
            tvEventFieldDepartmentValue.text = clickedEvent.department
            tvEventFieldOrganiserValue.text = clickedEvent.organizer
            tvEventFielStartTimeValue.text = clickedEvent.start
            tvEventFieldEndTimeValue.text = clickedEvent.end
            tvEventFieldDescriptionValue.text = clickedEvent.description
        }
        viewModel.registerEventStudentResult.observe(this@EventDetailsActivity, Observer { response ->
            when(response){
                is Resource.Success -> {
                    progressDialog.hide()
                    binding.btnRegisterForEvent.setEnabled(false)
                }
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Error -> {
                    progressDialog.hide()
                    response.message?.let{
                        Snackbar.make(binding.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
        binding.btnRegisterForEvent.setOnClickListener {
            viewModel.registerEventStudent(clickedEvent._id)
        }
    }
}