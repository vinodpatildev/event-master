package com.vinodpatildev.eventmaster.presentation.ui.registered_created

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.ProgressDialog
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentRegisteredCreatedBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.adapter.EventListAdapter
import com.vinodpatildev.eventmaster.presentation.ui.event.details.EventDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisteredCreatedFragment : Fragment() {
    private  var binding: FragmentRegisteredCreatedBinding? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mainActivity: MainActivity
    companion object {
        val CALLER_NAME = "created_registered_events"
        fun newInstance() = RegisteredCreatedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisteredCreatedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)


        when(mainActivity.viewModel.user){
            "admin" -> {
                mainActivity.viewModel.getEventsCreatedAdmin()
                binding?.rvRegisteredCreatedEventList?.layoutManager = LinearLayoutManager(this.context)
                mainActivity.viewModel.eventsCreatedListAdmin.observe(viewLifecycleOwner, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            binding?.rvRegisteredCreatedEventList?.visibility = View.VISIBLE
                            binding?.rvRegisteredCreatedEventList?.adapter = EventListAdapter(this.requireContext(),response.data!!) { clickedEvent: Event ->
                                onEventCardClicked(clickedEvent)
                            }
//                            progressDialog.hide()
                            binding?.shimmerViewContainer?.stopShimmer()
                            if(binding?.swipeRefreshLayout?.isRefreshing == true){
                                binding?.swipeRefreshLayout?.isRefreshing = false
                            }
                        }
                        is Resource.Loading -> {
//                            progressDialog.show()
                            binding?.shimmerViewContainer?.startShimmer()
                        }
                        is Resource.Error -> {
                            binding?.rvRegisteredCreatedEventList?.adapter = EventListAdapter(this.requireContext(),listOf<Event>()) { clickedEvent: Event ->
                                onEventCardClicked(clickedEvent)
                            }
//                            progressDialog.hide()
                            binding?.shimmerViewContainer?.stopShimmer()
                            Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            "student" -> {
                mainActivity.viewModel.getEventsRegisteredStudent()
                binding?.rvRegisteredCreatedEventList?.layoutManager = LinearLayoutManager(this.context)
                mainActivity.viewModel.eventRegisteredListStudent.observe(viewLifecycleOwner, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            binding?.rvRegisteredCreatedEventList?.visibility = View.VISIBLE
                            binding?.rvRegisteredCreatedEventList?.adapter = EventListAdapter(this.requireContext(),response.data!!) { clickedEvent: Event ->
                                onEventCardClicked(clickedEvent)
                            }
//                            progressDialog.hide()
                            binding?.shimmerViewContainer?.stopShimmer()
                            if(binding?.swipeRefreshLayout?.isRefreshing == true){
                                binding?.swipeRefreshLayout?.isRefreshing = false
                            }
                        }
                        is Resource.Loading -> {
//                            progressDialog.show()
                            binding?.shimmerViewContainer?.startShimmer()
                        }
                        is Resource.Error -> {
                            binding?.rvRegisteredCreatedEventList?.adapter = EventListAdapter(this.requireContext(),listOf<Event>()) { clickedEvent: Event ->
                                onEventCardClicked(clickedEvent)
                            }
//                            progressDialog.hide()
                            binding?.shimmerViewContainer?.stopShimmer()
                            Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            // Load data here
            // Once data is loaded, call swipeRefreshLayout.isRefreshing = false to stop the refreshing animation
            binding?.rvRegisteredCreatedEventList?.visibility = View.GONE
            when(mainActivity.viewModel.user){
                "admin" -> {
                    mainActivity.viewModel.adminData?.let {
                        mainActivity.viewModel.reloadCreatedEvents(
                            it._id)
                    }
                }
                "student" -> {
                    mainActivity.viewModel.studentData?.let {
                        mainActivity.viewModel.reloadRegisteredEvents(
                            it._id)
                    }
                }
            }
        }
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                binding?.swipeRefreshLayout?.isRefreshing = true
                binding?.swipeRefreshLayout?.setOnRefreshListener(null)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding?.rvRegisteredCreatedEventList)
    }

    private fun onEventCardClicked(clickedEvent: Event) {
        val eventDetailsIntent = Intent(this.context,EventDetailsActivity::class.java)
        eventDetailsIntent.putExtra(Event.TAG,clickedEvent)
        eventDetailsIntent.putExtra("caller",RegisteredCreatedFragment.CALLER_NAME)
        startActivity(eventDetailsIntent)
    }
    override fun onResume() {
        super.onResume()
        when(mainActivity.viewModel.user){
            "admin" -> {
                (activity as AppCompatActivity).supportActionBar?.setTitle("Created Events")
            }
            "student" -> {
                (activity as AppCompatActivity).supportActionBar?.setTitle("Registered Events")
            }
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(mainActivity,MainActivity::class.java))
                mainActivity.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}