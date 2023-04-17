package com.vinodpatildev.eventmaster.data.model

import com.google.gson.annotations.SerializedName

data class Admin(
    @SerializedName("db_version")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name:String,
    @SerializedName("profile_image_url")
    val profile_image_url:String,
)