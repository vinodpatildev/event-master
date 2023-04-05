package com.vinodpatildev.eventmaster.data.repository.datasource

import com.vinodpatildev.eventmaster.data.model.ApiResponse
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getEvents(cookiesData:String, ):Response<List<Event>>
    suspend fun getNotifications(cookiesData:String, ):Response<List<Notification>>
    suspend fun signInStudent(username: String, password: String) : Response<Student>
    suspend fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String) : Response<Student>
    suspend fun signOutStudent() : Response<Void>
    suspend fun updateStudentData(cookies: String,
                                  studentId: String,
                                  name: String,
                                  registration_no: String,
                                  dob: String,
                                  mobile_no: String,
                                  year: String,
                                  department: String,
                                  division: String,
                                  passing_year: String): Response<Student>
    suspend fun updateStudentPassword(cookies: String,
                                      studentId: String,
                                      oldPassword: String,
                                      newPassword: String):Response<Void>

}