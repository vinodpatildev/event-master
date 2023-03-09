package com.vinodpatildev.eventmaster.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event

class EventListAdapter(
    private val events:List<Event>,
    private val clickListener:(Event)->Unit
): RecyclerView.Adapter<EventListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.event_list_card,parent,false)
        return EventListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        holder.setViewHolderData(events[position],clickListener)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
class EventListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setViewHolderData(
        event:Event,
        clickListener: (Event) -> Unit

    ){
        itemView.findViewById<TextView>(R.id.tvEventName).text = event.eventName
        itemView.findViewById<TextView>(R.id.tvEventDate).text = event.eventDate
        itemView.findViewById<TextView>(R.id.tvEventOrganizerDepartment).text = event.eventOrganizerDepartment
        itemView.findViewById<TextView>(R.id.tvEventOrganizer).text = event.eventOrganizer
        itemView.findViewById<TextView>(R.id.tvEventDetails).text = event.eventDetails


        itemView.setOnClickListener({
            clickListener(event)
        })
    }
}