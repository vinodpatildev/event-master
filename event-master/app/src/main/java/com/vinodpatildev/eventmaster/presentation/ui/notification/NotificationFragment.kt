package com.vinodpatildev.eventmaster.presentation.ui.notification

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentNotificationBinding
import com.vinodpatildev.eventmaster.databinding.FragmentProfileBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.adapter.EventListAdapter
import com.vinodpatildev.eventmaster.presentation.adapter.NotificationListAdapter
import com.vinodpatildev.eventmaster.presentation.ui.event.details.EventDetailsActivity
import com.vinodpatildev.eventmaster.presentation.ui.profile.ProfileFragment
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var binding : FragmentNotificationBinding? = null
    companion object {
        fun newInstance() = NotificationFragment()
    }
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        binding = FragmentNotificationBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)

        mainActivity.viewModel.getNotifications()
        binding?.rvNotificationsList?.layoutManager = LinearLayoutManager(this.context)
        mainActivity.viewModel.notificationList.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    binding?.rvNotificationsList?.visibility = View.VISIBLE
                    binding?.rvNotificationsList?.adapter = NotificationListAdapter(response.data!!) { clickedNotification: Notification ->
                        onNotificationCardClicked(clickedNotification)
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
                    binding?.rvNotificationsList?.adapter = NotificationListAdapter(listOf<Notification>()) { clickedNotification: Notification ->
                        onNotificationCardClicked(clickedNotification)
                    }
//                    progressDialog.hide()
                    binding?.shimmerViewContainer?.stopShimmer()
                    Toast.makeText(context,response.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            // Load data here
            // Once data is loaded, call swipeRefreshLayout.isRefreshing = false to stop the refreshing animation
            binding?.rvNotificationsList?.visibility = View.GONE
            mainActivity.viewModel.reloadNotifications()
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
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding?.rvNotificationsList)
    }
    private fun onNotificationCardClicked(clickedNotification: Notification) {
//        Toast.makeText(this.context, "Notification [${clickedNotification._id}] is clicked",Toast.LENGTH_LONG).show()
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Notifications")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}