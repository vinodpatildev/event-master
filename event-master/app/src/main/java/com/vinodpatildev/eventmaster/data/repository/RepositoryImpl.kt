package com.vinodpatildev.eventmaster.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.createDataStore
import com.google.android.gms.common.api.Api
import com.vinodpatildev.eventmaster.data.model.*
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository
import retrofit2.Response

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): Repository {
    override suspend fun getEvents(cookiesData:String, ): Resource<List<Event>> {
        return responseToResourceEvents(remoteDataSource.getEvents(cookiesData ))
    }

    override suspend fun getNotifications(cookiesData:String, ): Resource<List<Notification>> {
        return responseToResourceNotifications(remoteDataSource.getNotifications(cookiesData));
    }


    override suspend fun signInStudent(
        username: String,
        password: String
    ): Resource<ApiResponse<Student>> {
        val response = remoteDataSource.signInStudent(username,password)
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)
        return responseToResourceStudent(response,sidValue!!)
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
    ): Resource<ApiResponse<Student>> {
        val response = remoteDataSource.signUpStudent(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year)
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)
        return responseToResourceStudent(response,sidValue!!)
    }

    override suspend fun singOutStudent(): Resource<Boolean> {
        val response = remoteDataSource.signOutStudent()
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error("Not able to sign out.")
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
    ): Resource<ApiResponse<Student>> {
        val response = remoteDataSource.updateStudentData(cookies,
            studentId,
            name,
            registration_no,
            dob,
            mobile_no,
            year,
            department,
            division,
            passing_year)
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)
        return responseToResourceStudent(response,sidValue!!)
    }

    override suspend fun updateStudentPassword(
        cookies: String,
        studentId: String,
        oldPassword: String,
        newPassword: String
    ): Resource<Boolean> {
        val response = remoteDataSource.updateStudentPassword(
            cookies,
            studentId,
            oldPassword,
            newPassword
        )
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error("Not able to update password.")
    }

    fun responseToResourceEvents(response: Response<List<Event>>):Resource<List<Event>>{
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
    fun responseToResourceNotifications(response: Response<List<Notification>>):Resource<List<Notification>>{
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
    fun responseToResourceStudent(response: Response<Student>, cookies:String):Resource<ApiResponse<Student>>{
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(ApiResponse<Student>(result,cookies))
            }
        }
        return Resource.Error(response.message())
    }
    fun responseToResourceAdmin(response: Response<Admin>):Resource<Admin>{
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}