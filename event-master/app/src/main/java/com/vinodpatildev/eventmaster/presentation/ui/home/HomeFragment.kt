package com.vinodpatildev.eventmaster.presentation.ui.home

import android.app.ProgressDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentHomeBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.adapter.EventListAdapter
import com.vinodpatildev.eventmaster.presentation.ui.event.details.EventDetailsActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private  var binding: FragmentHomeBinding? = null
    private lateinit var progressDialog: ProgressDialog
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)

        mainActivity.viewModel.getEvents()
        binding?.rvHomeEventList?.layoutManager = LinearLayoutManager(this.context)
        mainActivity.viewModel.eventList.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    binding?.rvHomeEventList?.visibility = View.VISIBLE
                    binding?.rvHomeEventList?.adapter = EventListAdapter(this.requireContext(),response.data!!) { clickedEvent: Event ->
                        onEventCardClicked(clickedEvent)
                    }
//                    progressDialog.hide()
                    binding?.shimmerViewContainer?.stopShimmer()
                    if(binding?.swipeRefreshLayout?.isRefreshing == true){
                        binding?.swipeRefreshLayout?.isRefreshing = false
                    }
                }
                is Resource.Loading -> {
//                    progressDialog.show()
                    binding?.shimmerViewContainer?.startShimmer()
                }
                is Resource.Error -> {
                    binding?.rvHomeEventList?.adapter = EventListAdapter(this.requireContext(),listOf<Event>()) { clickedEvent: Event ->
                        onEventCardClicked(clickedEvent)
                    }
//                    progressDialog.hide()
                    binding?.shimmerViewContainer?.stopShimmer()
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                }
            }

        })

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            // Load data here
            // Once data is loaded, call swipeRefreshLayout.isRefreshing = false to stop the refreshing animation
            binding?.rvHomeEventList?.visibility = View.GONE
            mainActivity.viewModel.reloadEvents()
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
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding?.rvHomeEventList)
    }

    private fun onEventCardClicked(clickedEvent: Event) {
        val eventDetailsIntent = Intent(this.context,EventDetailsActivity::class.java)
        eventDetailsIntent.putExtra(Event.TAG,clickedEvent)
        startActivity(eventDetailsIntent)
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Event Master")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}