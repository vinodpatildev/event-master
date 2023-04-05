package com.vinodpatildev.eventmaster.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Student(
    @SerializedName("db_version")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("division")
    val division: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("passing_year")
    val passing_year: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("registration_no")
    val registration_no: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("year")
    val year: String
): Serializable {
    companion object {
        val TAG = "student"
    }
}