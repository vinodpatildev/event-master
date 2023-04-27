package com.vinodpatildev.eventmaster.data.repository.datasource

import android.net.Uri
import com.vinodpatildev.eventmaster.data.model.Admin
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.ResultData
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface RemoteDataSource {
    suspend fun getEvents(cookiesData:String, ):Response<List<Event>>
    suspend fun getNotifications(cookiesData:String, ):Response<List<Notification>>
    suspend fun signInStudent(username: String, password: String) : Response<Student>
    suspend fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String, profile_image_url:String) : Response<Student>
    suspend fun signOutStudent() : Response<Void>
    suspend fun forgetPasswordStudent(username:String, email:String):Response<Void>
    suspend fun resetPasswordStudent(email:String, password:String, otp:String):Response<Void>
    suspend fun updateDataStudent(cookies: String,
                                  studentId: String,
                                  name: String,
                                  registration_no: String,
                                  dob: String,
                                  mobile_no: String,
                                  year: String,
                                  department: String,
                                  division: String,
                                  passing_year: String,
                                  profile_image_url:String
    ): Response<Student>
    suspend fun updatePasswordStudent(cookies: String, studentId: String, oldPassword: String, newPassword: String):Response<Void>
    suspend fun registerForEventStudent(cookies:String,studentId:String, eventId:String) : Response<Void>
    suspend fun markAttendanceForEventStudent(cookies:String,studentId:String, eventId:String) : Response<Void>

    suspend fun downloadEventCertificateStudent(cookies:String,studentId:String, eventId:String): Response<ResponseBody>

    suspend fun getEventsRegisteredStudent(cookiesData:String, studentId: String) : Response<List<Event>>

    suspend fun signInAdmin(username: String, password: String) : Response<Admin>
    suspend fun registerAdminBySuperAdmin(cookies: String, username: String, email: String, password: String, name:String) : Response<Void>
    suspend fun signOutAdmin() : Response<Void>
    suspend fun getEventsCreatedAdmin(cookiesData:String, adminId: String) : Response<List<Event>>
    suspend fun createEventsAdmin(cookiesData : String, adminId:String, title : String, type : String, description : String, location : String, event_link : String, start: String, end: String, department : String, organizer : String, longitude : String, latitude : String): Response<Event>
    suspend fun getEventsReportAdmin(cookiesData: String, eventId:String):Response<List<Student>>
    suspend fun updatePasswordAdmin(cookies: String,
                                      adminId: String,
                                      oldPassword: String,
                                      newPassword: String):Response<Void>

    suspend fun uploadProfilePicture(userId: String, imageUri: Uri ): ResultData<String>
    suspend fun updateProfilePictureStudent(cookiesData: String, studentId:String, url:String) : Response<String>
    suspend fun updateProfilePictureAdmin(cookiesData: String, adminId:String, url:String) : Response<String>
    suspend fun forgetPasswordAdmin(username:String, email:String):Response<Void>
    suspend fun resetPasswordAdmin(email:String, password:String, otp:String):Response<Void>

}