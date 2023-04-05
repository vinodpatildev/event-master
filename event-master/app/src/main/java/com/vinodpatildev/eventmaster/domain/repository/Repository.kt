package com.vinodpatildev.eventmaster.domain.repository


import com.vinodpatildev.eventmaster.data.model.ApiResponse
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.Resource

interface Repository {
    suspend fun getEvents(cookiesData:String ):Resource<List<Event>>
    suspend fun getNotifications(cookiesData:String ):Resource<List<Notification>>
    suspend fun signInStudent(username: String, password: String):Resource<ApiResponse<Student>>
    suspend fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String):Resource<ApiResponse<Student>>
    suspend fun singOutStudent(): Resource<Boolean>
    suspend fun updateStudentData(cookies:String, studentId:String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String) : Resource<ApiResponse<Student>>
    suspend fun updateStudentPassword(cookies:String, studentId:String, oldPassword:String, newPassword:String ) : Resource<Boolean>
}