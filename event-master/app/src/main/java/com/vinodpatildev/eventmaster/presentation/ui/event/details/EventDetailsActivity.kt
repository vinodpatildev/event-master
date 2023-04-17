package com.vinodpatildev.eventmaster.presentation.ui.event.details

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityEventDetailsBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.ui.geofence.GeofenceActivity
import com.vinodpatildev.eventmaster.presentation.ui.registered_created.RegisteredCreatedFragment
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

    private lateinit var clickedEvent : Event
    private lateinit var caller_name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Processing event....")
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

        clickedEvent = intent.getSerializableExtra(Event.TAG) as Event
        caller_name= intent.getStringExtra("caller").toString()

        binding.apply {
            ivEventPicture.setImageDrawable(ContextCompat.getDrawable(this@EventDetailsActivity,imageArray[(Math.abs(clickedEvent._id.hashCode()))%imageArray.size]))
            tvEventFieldIdValue.text = clickedEvent._id
            tvEventFieldStatusValue.text = clickedEvent.state
            tvEventFieldTitleValue.text = clickedEvent.title
            tvEventFieldTypeValue.text = clickedEvent.type
            tvEventFieldDepartmentValue.text = clickedEvent.department
            tvEventFieldOrganiserValue.text = clickedEvent.organizer
            tvEventFieldLocationValue.text = clickedEvent.location
            tvEventFielStartTimeValue.text = clickedEvent.start
            tvEventFieldEndTimeValue.text = clickedEvent.end
            tvEventFieldDescriptionValue.text = clickedEvent.description
        }

        when(viewModel.user){
            "admin"->{
                if(caller_name == RegisteredCreatedFragment.CALLER_NAME){
                    binding.btnRegisterAttendDownloadCertificateEvent.setOnClickListener {
                        val eventReportIntent = Intent(this,EventReportActivity::class.java)
                        eventReportIntent.putExtra(Event.TAG,clickedEvent)
                        startActivity(eventReportIntent)
                    }
                    if(clickedEvent.state == Event.UPCOMING || clickedEvent.state == Event.LIVE){
                        binding.btnRegisterAttendDownloadCertificateEvent.text = "REGISTRATION REPORT"

                    }else if(clickedEvent.state == Event.FINISHED){
                        binding.btnRegisterAttendDownloadCertificateEvent.text = "STUDENT REPORT"

                    }
                }else{
                    binding.btnRegisterAttendDownloadCertificateEvent.visibility = View.GONE
                }
            }
            "student"->{
                binding.btnRegisterAttendDownloadCertificateEvent.setOnClickListener {
                    if(clickedEvent.state == Event.UPCOMING){
                        viewModel.registerEventStudent(clickedEvent._id)
                    }else if(clickedEvent.state == Event.LIVE){
                        //attend the event
                        // TODO : Mark attendance using geofence
                        val geofenceActivityIntent = Intent(this,GeofenceActivity::class.java)
                        geofenceActivityIntent.putExtra(Event.TAG,clickedEvent)
                        startActivity(geofenceActivityIntent)
                    }else if(clickedEvent.state == Event.FINISHED){
                        viewModel.downloadEventCertificateStudent(this,clickedEvent._id)
                    }
                }
                if(caller_name == RegisteredCreatedFragment.CALLER_NAME){
                    if(clickedEvent.state == Event.LIVE){
                        binding.btnRegisterAttendDownloadCertificateEvent.text = "MARK ATTENDANCE"
                        // TODO : Mark attendance using geofence
                    }else if(clickedEvent.state == Event.FINISHED){
                        binding.btnRegisterAttendDownloadCertificateEvent.text = "DOWNLOAD CERTIFICATE"
                        viewModel.downloadEventCertificateResultStudent.observe(this@EventDetailsActivity, Observer { response ->
                            when(response){
                                is Resource.Success -> {
                                    progressDialog.hide()
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
                    }else{
                        binding.btnRegisterAttendDownloadCertificateEvent.visibility = View.GONE
                    }
                } else {
                    if(clickedEvent.state == Event.UPCOMING){
                        binding.btnRegisterAttendDownloadCertificateEvent.text = "REGISTER"
                        viewModel.registerEventResultStudent.observe(this@EventDetailsActivity, Observer { response ->
                            when(response){
                                is Resource.Success -> {
                                    progressDialog.hide()
//                              binding.btnRegisterForEvent.setEnabled(false)
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
                    }else{
                        binding.btnRegisterAttendDownloadCertificateEvent.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnShareEvent.setOnClickListener {
            shareEventDetails(clickedEvent)
        }
    }
    override fun onResume() {
        super.onResume()
        when(viewModel.user){
            "admin" -> {
                supportActionBar?.setTitle("Event Details")
            }
            "student" -> {
                supportActionBar?.setTitle("Event Details")
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareEventDetails(event: Event) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        val shareMessage = "Dear all students attend this event,\n\n" +
                "𝐓𝐢𝐭𝐥𝐞 : ${event.title}\n" +
                "𝐓𝐲𝐩𝐞 : ${event.type}\n" +
                "𝐃𝐞𝐬𝐜𝐫𝐢𝐩𝐭𝐢𝐨𝐧 : ${event.description}\n" +
                "𝐃𝐞𝐩𝐚𝐫𝐭𝐦𝐞𝐧𝐭 : ${event.department}\n" +
                "𝐎𝐫𝐠𝐚𝐧𝐢𝐳𝐞𝐫 : ${event.organizer}\n" +
                "𝐒𝐭𝐚𝐫𝐭 𝐓𝐢𝐦𝐞 : ${event.start}\n" +
                "𝐄𝐧𝐝 𝐓𝐢𝐦𝐞 : ${event.end}\n" +
                "𝐋𝐨𝐜𝐚𝐭𝐢𝐨𝐧 : ${event.location}\n" +
                "𝐄𝐯𝐞𝐧𝐭 𝐋𝐢𝐧𝐤 : ${event.event_link}\n"

        intent.putExtra(Intent.EXTRA_TEXT,shareMessage)
        val chooser = Intent.createChooser(intent,"Share this event using...")
        startActivity(chooser)
    }




}