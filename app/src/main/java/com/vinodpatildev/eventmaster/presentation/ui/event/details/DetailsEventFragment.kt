package com.vinodpatildev.eventmaster.presentation.ui.event.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.databinding.FragmentDetailsEventBinding
import com.vinodpatildev.eventmaster.databinding.FragmentEditEventBinding

class DetailsEventFragment : Fragment() {
    private lateinit var binding :FragmentDetailsEventBinding
    companion object {
        fun newInstance() = DetailsEventFragment()
    }

    private lateinit var viewModel: DetailsEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsEventBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickedEvent: Event = arguments?.getSerializable(Event.TAG) as Event
        binding.tvEventFieldNameValue.text = clickedEvent.eventName
        binding.tvEventFieldDateValue.text = clickedEvent.eventDate
        binding.tvEventFieldOrganizerDepartmentValue.text = clickedEvent.eventOrganizerDepartment
        binding.tvEventFieldOrganizerValue.text = clickedEvent.eventOrganizer
        binding.tvEventFieldDetailsValue.text = clickedEvent.eventDetails
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsEventViewModel::class.java)
        // TODO: Use the ViewModel
    }

}