package com.vinodpatildev.eventmaster.presentation.ui.event.details

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityEventDetailsBinding
import com.vinodpatildev.eventmaster.databinding.ActivityEventReportBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.presentation.adapter.EventListAdapter
import com.vinodpatildev.eventmaster.presentation.adapter.EventReportListAdapter

@AndroidEntryPoint
class EventReportActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityEventReportBinding
    private lateinit var progressDialog:  ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Processing....")
        progressDialog.setCancelable(false)

        val clickedEvent: Event = intent.getSerializableExtra(Event.TAG) as Event

        binding.apply {
            tvEventReportFieldIdValue.text = clickedEvent._id
            tvEventReportFieldTitleValue.text = clickedEvent.title
            tvEventReprtFieldStatusValue.text = clickedEvent.state
            tvEventReportFieldDescriptionValue.text = clickedEvent.description
        }
        viewModel.getEventReportAdmin(clickedEvent._id)
        binding?.rvRegisteredCreatedEventList?.layoutManager = LinearLayoutManager(this)
        viewModel.eventReportAdminResult.observe(this, Observer { response->
            when(response){
                is Resource.Success -> {

//                    Snackbar.make(binding.root,"loaded report : size :${response.data.toString()}",Snackbar.LENGTH_LONG).show()
                    binding.rvRegisteredCreatedEventList?.visibility = View.VISIBLE
                    binding.rvRegisteredCreatedEventList?.adapter = EventReportListAdapter(this,response.data!!) { clickedStudent: Student ->
                        onStudentReportCardClicked(clickedStudent)
                    }
//                    progressDialog.hide();
                    binding.shimmerViewContainer.stopShimmer()
                }
                is Resource.Loading -> {
//                    progressDialog.show()
                    binding.shimmerViewContainer.startShimmer()
                }
                is Resource.Error -> {
//                    progressDialog.hide()
                    binding.shimmerViewContainer.stopShimmer()
                    response.message?.let{
                        Snackbar.make(binding.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun onStudentReportCardClicked(clickedStudent: Student) {
    }
    override fun onResume() {
        super.onResume()
        supportActionBar?.setTitle("Registration Report")
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
}