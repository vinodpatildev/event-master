package com.vinodpatildev.eventmaster.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Student

class EventReportListAdapter (
    private val ctx: Context,
    private val students:List<Student>,
    private val clickListener:(Student)->Unit
): RecyclerView.Adapter<EventReportListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventReportListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.event_report_student_list_card,parent,false)
        return EventReportListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: EventReportListViewHolder, position: Int) {
        holder.setViewHolderData(ctx,students[position],clickListener)
    }

    override fun getItemCount(): Int {
        return students.size
    }
}
class EventReportListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setViewHolderData(
        ctx: Context,
        student: Student,
        clickListener: (Student) -> Unit

    ){
        itemView.findViewById<TextView>(R.id.tvStudentIdValue).text = student._id
        itemView.findViewById<TextView>(R.id.tvStudentNameValue).text = student.name
        itemView.findViewById<TextView>(R.id.tvStudentEmailValue).text = student.email

        if(student.present){
            itemView.findViewById<TextView>(R.id.tv_registered_present_status).text = "PRESENT"
        }

        itemView.setOnClickListener({
            clickListener(student)
        })
    }
}