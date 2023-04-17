package com.vinodpatildev.eventmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName="created_events")
data class CreatedEvent(
    @SerializedName("db_version")
    val __v: Int?,
    @PrimaryKey
    @SerializedName("_id")
    val _id: String,
    @SerializedName("adminId")
    val adminId: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("department")
    val department: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("end")
    val end: String?,
    @SerializedName("eventLink")
    val event_link: String?,
    @SerializedName("organizer")
    val organizer: String?,
    @SerializedName("start")
    val start: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type : String?,
    @SerializedName("updatedAt")
    val updatedAt : String?,
    @SerializedName("location")
    val location : String?,
    @SerializedName("longitude")
    val longitude : String?,
    @SerializedName("latitude")
    val latitude : String?,

    ) : Serializable {
    companion object {
        val TAG = "event";
        val UPCOMING = "upcoming_events";
        val LIVE = "live_events";
        val FINISHED = "finished_events";
    }
}