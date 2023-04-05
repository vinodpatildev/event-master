package com.vinodpatildev.eventmaster.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Notification

class NotificationListAdapter(
    private val notifications :List<Notification>,
    private val clickListener:(Notification)->Unit
): RecyclerView.Adapter<NotificationListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.notification_list_card,parent,false)
        return NotificationListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: NotificationListViewHolder, position: Int) {
        holder.setViewHolderData(notifications[position],clickListener)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}
class NotificationListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setViewHolderData(
        notification: Notification,
        clickListener: (Notification) -> Unit

    ){
        itemView.findViewById<TextView>(R.id.tvTitle).text = notification.title
        itemView.findViewById<TextView>(R.id.tvBody).text = notification.body
        itemView.findViewById<TextView>(R.id.tvPriority).text = notification.priority


        itemView.setOnClickListener({
            clickListener(notification)
        })
    }
}