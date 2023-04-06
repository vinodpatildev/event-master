package com.vinodpatildev.eventmaster.data.api

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

    @POST("api/students/forget")
    suspend fun forgetPasswordStudent(
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ):Response<Void>

    @POST("api/students/reset")
    suspend fun resetPasswordStudent(
        @Body resetPasswordRequest: ResetPasswordRequest
    ):Response<Void>

    @GET("api/events")
    suspend fun getEventsStudent(
        @Header("Cookie") sessionIdAndToken: String
    ): Response<List<Event>>

    @GET("api/notifications")
    suspend fun getNotificationsStudent(
        @Header("Cookie") sessionIdAndToken: String
    ) : Response<List<Notification>>

    @POST("api/students/update/{studentId}")
    suspend fun updateDataStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateStudentDataRequest: UpdateStudentDataRequest,
        @Path("studentId") studentId: String
    ) : Response<Student>

    @POST("api/students/password/{studentId}")
    suspend fun updatePasswordStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateStudentPasswordRequest: UpdateStudentPasswordRequest,
        @Path("studentId") studentId: String
    ):Response<Void>

    @POST("api/students/register")
    suspend fun registerForEventStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Body registerEventStudentRequest: RegisterEventStudentRequest
    ) : Response<Void>
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

data class ForgetPasswordRequest(
    val username: String,
    val email: String
)

data class ResetPasswordRequest(
    val email:String,
    val password: String,
    val otp: String
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

data class RegisterEventStudentRequest(
    val studentId:String,
    val eventId:String
)
