package com.vinodpatildev.eventmaster.domain.repository


import android.content.Context
import android.net.Uri
import com.vinodpatildev.eventmaster.data.api.CreateEventRequestAdmin
import com.vinodpatildev.eventmaster.data.model.*
import com.vinodpatildev.eventmaster.data.util.Resource
import retrofit2.Response
import java.io.File

interface Repository {
    suspend fun getEvents(cookiesData:String ):Resource<List<Event>>
    suspend fun reloadEvents(cookiesData:String):Resource<List<Event>>
    suspend fun getNotifications(cookiesData:String ):Resource<List<Notification>>
    suspend fun reloadNotifications(cookiesData:String ):Resource<List<Notification>>
    suspend fun signInStudent(username: String, password: String):Resource<ApiResponse<Student>>
    suspend fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String,profile_image_url:String):Resource<ApiResponse<Student>>
    suspend fun singOutStudent(): Resource<Boolean>
    suspend fun forgetPasswordStudent(username:String, email:String): Resource<Boolean>
    suspend fun resetPasswordStudent(email:String, password:String, otp:String): Resource<Boolean>
    suspend fun updateDataStudent(cookies:String, studentId:String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String,profile_image_url:String) : Resource<ApiResponse<Student>>
    suspend fun updatePasswordStudent(cookies:String, studentId:String, oldPassword:String, newPassword:String ) : Resource<Boolean>
    suspend fun updatePasswordAdmin(cookies:String, adminId:String, oldPassword:String, newPassword:String ) : Resource<Boolean>
    suspend fun registerForEventStudent(cookies:String, studentId:String, eventId:String):Resource<Boolean>
    suspend fun markAttendanceForEventStudent(cookies:String, studentId:String, eventId:String):Resource<Boolean>
    suspend fun downloadEventCertificateStudent(context: Context, cookies:String, studentId:String, eventId:String) : Resource<String>
    suspend fun getEventsRegisteredStudent(cookiesData: String, studentId: String) : Resource<List<Event>>
    suspend fun reloadEventsRegisteredStudent(cookiesData: String, studentId: String) : Resource<List<Event>>
    suspend fun signInAdmin(username: String, password: String):Resource<ApiResponse<Admin>>
    suspend fun registerAdminBySuperAdmin(cookiesData:String, username: String, email: String, password: String, name:String):Resource<Boolean>
    suspend fun singOutAdmin(): Resource<Boolean>
    suspend fun getEventsCreatedAdmin(cookiesData:String, adminId: String) : Resource<List<Event>>
    suspend fun reloadEventsCreatedAdmin(cookiesData:String, adminId: String) : Resource<List<Event>>
    suspend fun CreateEventAdmin(cookiesData: String, adminId:String,title : String, type : String, description : String, location : String, event_link : String, start: String, end: String, department : String, organizer : String, longitude : String, latitude : String):Resource<Event>
    suspend fun getEventReportAdmin(cookiesData: String,eventId:String):Resource<List<Student>>
    suspend fun uploadProfilePicture(userId: String, imageUri: Uri): Resource<String>
    suspend fun updateProfilePictureStudent( cookiesData: String, studentId: String, url: String): Resource<String>
    suspend fun updateProfilePictureAdmin( cookiesData: String, adminId: String, url: String): Resource<String>
    suspend fun forgetPasswordAdmin(username:String, email:String): Resource<Boolean>
    suspend fun resetPasswordAdmin(email:String, password:String, otp:String): Resource<Boolean>

}