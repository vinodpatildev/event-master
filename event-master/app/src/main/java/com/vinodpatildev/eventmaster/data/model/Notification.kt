package com.vinodpatildev.eventmaster.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Notification(
    @SerializedName("db_version")
    val __v: Int,
    @PrimaryKey
    @SerializedName("_id")
    val _id: String,
    @SerializedName("adminId")
    val adminId: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("channel")
    val channel: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("eventId")
    val eventId: String,
    @SerializedName("priority")
    val priority: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)