package com.vinodpatildev.eventmaster.data.model

import java.io.Serializable

data class Event(
    val eventId:Int,
    val eventName:String,
    val eventDate:String,
    val eventOrganizer:String,
    val eventOrganizerDepartment: String,
    val eventDetails:String,
):Serializable {
    companion object{
        val TAG = "event"
    }
}
