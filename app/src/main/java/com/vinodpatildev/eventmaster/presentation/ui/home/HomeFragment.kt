package com.vinodpatildev.eventmaster.presentation.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.databinding.FragmentHomeBinding
import com.vinodpatildev.eventmaster.presentation.ui.event.details.DetailsEventFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val events = listOf<Event>(
            Event(
                eventId = 1,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 2,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 3,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 4,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 5,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 6,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 7,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 8,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 9,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            ),
            Event(
                eventId = 10,
                eventName = "Expert session on “Career Opportunities and Skill Development”.",
                eventDate = "01/07/2022",
                eventOrganizer = "Mr.Vinod Patil",
                eventOrganizerDepartment = "Department of First Year B.Tech",
                eventDetails = "Expert session on “Career Opportunities and Skill Development” organized by Department of First Year B.Tech on 1st July, 2022",
            )
            )
        binding.rvHomeEventList.layoutManager = LinearLayoutManager(this.context)
        binding.rvHomeEventList.adapter = EventListAdapter(events,{
            clickedEvent: Event -> onEventCardClicked(clickedEvent)
        })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun onEventCardClicked(clickedEvent: Event) {
        Toast.makeText(this.context, "Event [${clickedEvent.eventId}] is clicked",Toast.LENGTH_LONG).show()
        val mBundle = Bundle()
        mBundle.putSerializable(Event.TAG,clickedEvent)
        val mFragment = DetailsEventFragment.newInstance()
        mFragment.arguments = mBundle
        fragmentManager?.beginTransaction()
            ?.replace(R.id.MainContainer,mFragment)
            ?.commitNow()
    }
}