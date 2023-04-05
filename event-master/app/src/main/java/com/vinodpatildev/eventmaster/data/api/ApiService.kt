package com.vinodpatildev.eventmaster.data.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/students/login")
    suspend fun signInStudent(
        @Body singInRequest: SingInRequest
    ):Response<Student>

    @POST("api/students/signup")
    suspend fun signUpStudent(
        @Body signUpRequest: SignUpRequest
    ):Response<Student>

    @POST("api/students/logout")
    suspend fun signOutStudent():Response<Void>

    @GET("api/events")
    suspend fun getEvents(
        @Header("Cookie") sessionIdAndToken: String
    ): Response<List<Event>>

    @GET("api/notifications")
    suspend fun getNotifications(
        @Header("Cookie") sessionIdAndToken: String
    ) : Response<List<Notification>>

    @POST("api/students/update/{studentId}")
    suspend fun updateStudentData(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateStudentDataRequest: UpdateStudentDataRequest,
        @Path("studentId") studentId: String
    ) : Response<Student>

    @POST("api/students/password/{studentId}")
    suspend fun updateStudentPassword(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateStudentPasswordRequest: UpdateStudentPasswordRequest,
        @Path("studentId") studentId: String
    ):Response<Void>
}

data class SingInRequest(
    val username: String,
    val password: String
)

data class SignUpRequest(
    val username: String,
    val email:String,
    val password: String,
    val name:String,
    val registration_no:String,
    val dob:String,
    val mobile:String,
    val year:String,
    val department:String,
    val division:String,
    val passing_year:String
)

data class UpdateStudentDataRequest(
    val name:String,
    val registration_no:String,
    val dob:String,
    val mobile:String,
    val year:String,
    val department:String,
    val division:String,
    val passing_year:String
)

data class UpdateStudentPasswordRequest(
    val old_password: String,
    val new_password:String
)
