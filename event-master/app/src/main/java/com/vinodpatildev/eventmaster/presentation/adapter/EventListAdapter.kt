package com.vinodpatildev.eventmaster.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import kotlin.random.Random

class EventListAdapter(
    private val ctx:Context,
    private val events:List<Event>,
    private val clickListener:(Event)->Unit
): RecyclerView.Adapter<EventListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.event_list_card,parent,false)
        return EventListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        holder.setViewHolderData(ctx,events[position],clickListener)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
class EventListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    fun setViewHolderData(
        ctx:Context,
        event:Event,
        clickListener: (Event) -> Unit

    ){
        itemView.findViewById<ImageView>(R.id.ivEventPicture).setImageDrawable(ContextCompat.getDrawable(ctx,imageArray[(Math.abs(event._id.hashCode()))%imageArray.size]))
        itemView.findViewById<TextView>(R.id.tvEventName).text = event.title
        itemView.findViewById<TextView>(R.id.tvEventStartTime).text = event.start
        itemView.findViewById<TextView>(R.id.tvEventDepartment).text = event.department
        itemView.findViewById<TextView>(R.id.tvEventOrganizer).text = event.organizer
        itemView.findViewById<TextView>(R.id.tvEventDescription).text = event.description


        itemView.setOnClickListener({
            clickListener(event)
        })
    }
}