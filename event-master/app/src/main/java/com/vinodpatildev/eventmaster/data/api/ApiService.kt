package com.vinodpatildev.eventmaster.data.api

import com.vinodpatildev.eventmaster.data.model.Admin
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/events")
    suspend fun getEvents(
        @Header("Cookie") sessionIdAndToken: String
    ): Response<List<Event>>

    @GET("api/notifications")
    suspend fun getNotifications(
        @Header("Cookie") sessionIdAndToken: String
    ) : Response<List<Notification>>


    @POST("api/students/login")
    suspend fun signInStudent(
        @Body signInRequest: SignInRequest
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

    @POST("api/events/certificate")
    @Streaming
    suspend fun downloadEventCertificateStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Body downloadEventCertificateStudentRequest: DownloadEventCertificateStudentRequest
    ) : Response<ResponseBody>

    @GET("api/events/registered/{studentId}")
    suspend fun getEventsRegisteredStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Path("studentId") studentId: String
    ): Response<List<Event>>

    //Admin

    @POST("api/admins/login")
    suspend fun signInAdmin(
        @Body signInRequest: SignInRequest
    ):Response<Admin>

    @POST("api/admin/signup")
    suspend fun registerAdminBySuperAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Body signUpRequestAdmin: SignUpRequestAdmin
    ):Response<Void>

    @POST("api/admins/logout")
    suspend fun signOutAdmin():Response<Void>

    @GET("api/events/created/{adminId}")
    suspend fun getEventsCreatedAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Path("adminId") adminId: String
    ): Response<List<Event>>

    @POST("api/events/{adminId}")
    suspend fun createEventAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Path("adminId") adminId: String,
        @Body createEventRequestAdmin : CreateEventRequestAdmin
    ):Response<Event>

    @GET("api/events/report/{eventId}")
    suspend fun getEventsReportAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Path("eventId") eventId: String
    ) : Response<List<Student>>

    @POST("api/admins/password/{adminId}")
    suspend fun updatePasswordAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateStudentPasswordRequest: UpdateStudentPasswordRequest,
        @Path("studentId") studentId: String
    ):Response<Void>

    @POST("api/students/updateProfilePicture")
    suspend fun updateProfilePictureStudent(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateUserProfilePictureRequest: UpdateUserProfilePictureRequest,
    ) : Response<String>

    @POST("api/admins/updateProfilePicture")
    suspend fun updateProfilePictureAdmin(
        @Header("Cookie") sessionIdAndToken: String,
        @Body updateUserProfilePictureRequest: UpdateUserProfilePictureRequest,
    ) : Response<String>

    @POST("api/admins/forget")
    suspend fun forgetPasswordAdmin(
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ):Response<Void>

    @POST("api/admins/reset")
    suspend fun resetPasswordAdmin(
        @Body resetPasswordRequest: ResetPasswordRequest
    ):Response<Void>

}

data class SignInRequest(
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
    val passing_year:String,
    val profile_image_url:String
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
    val passing_year:String,
    val profile_image_url:String,
)

data class UpdateStudentPasswordRequest(
    val old_password: String,
    val new_password:String
)

data class RegisterEventStudentRequest(
    val studentId:String,
    val eventId:String
)

data class DownloadEventCertificateStudentRequest(
    val studentId: String,
    val eventId: String
)
data class SignUpRequestAdmin(
    val username: String,
    val email:String,
    val password: String,
    val name:String
)

data class CreateEventRequestAdmin(
    val title : String,
    val type : String,
    val description : String,
    val location : String,
    val event_link : String,
    val start: String,
    val end: String,
    val department : String,
    val organizer : String,
    val longitude : String,
    val latitude : String,
)

data class UpdateUserProfilePictureRequest(
    val userId:String,
    val url:String,
)