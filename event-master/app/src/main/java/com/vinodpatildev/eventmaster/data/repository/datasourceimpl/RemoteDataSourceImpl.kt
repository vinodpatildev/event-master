package com.vinodpatildev.eventmaster.data.repository.datasourceimpl

import com.vinodpatildev.eventmaster.data.api.*
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import okhttp3.Cookie
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Response
import java.net.CookieHandler
import java.net.CookieManager

class RemoteDataSourceImpl(private val apiService: ApiService): RemoteDataSource {
    override suspend fun getEvents(cookiesData:String, ): Response<List<Event>> {
        return apiService.getEvents(cookiesData)
    }

    override suspend fun getNotifications(cookiesData:String, ): Response<List<Notification>> {
        return apiService.getNotifications(cookiesData)
    }

    override suspend fun signInStudent(
        username: String,
        password: String
    ): Response<Student> {
        return apiService.signInStudent(SingInRequest(username,password))
    }

    override suspend fun signUpStudent(
        username: String,
        email: String,
        password: String,
        name: String,
        registration_no: String,
        dob: String,
        mobile_no: String,
        year: String,
        department: String,
        division: String,
        passing_year: String
    ): Response<Student> {
        return apiService.signUpStudent(SignUpRequest(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year))
    }

    override suspend fun signOutStudent(): Response<Void> {
        return apiService.signOutStudent()
    }

    override suspend fun updateStudentData(
        cookies: String,
        studentId: String,
        name: String,
        registration_no: String,
        dob: String,
        mobile_no: String,
        year: String,
        department: String,
        division: String,
        passing_year: String
    ): Response<Student> {
        return apiService.updateStudentData(cookies, UpdateStudentDataRequest(name,registration_no,dob,mobile_no,year,department,division,passing_year),studentId)
    }

    override suspend fun updateStudentPassword(
        cookies: String,
        studentId: String,
        oldPassword: String,
        newPassword: String
    ): Response<Void> {
        return apiService.updateStudentPassword(cookies, UpdateStudentPasswordRequest(oldPassword,newPassword),studentId)
    }

}