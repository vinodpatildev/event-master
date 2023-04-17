package com.vinodpatildev.eventmaster.data.repository.datasourceimpl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.vinodpatildev.eventmaster.data.api.*
import com.vinodpatildev.eventmaster.data.model.Admin
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.model.Notification
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.data.util.ResultData
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDataSourceImpl(private val apiService: ApiService): RemoteDataSource {
    override suspend fun getEvents(cookiesData: String): Response<List<Event>> {
        return apiService.getEvents(cookiesData)
    }

    override suspend fun getNotifications(cookiesData: String): Response<List<Notification>> {
        return apiService.getNotifications(cookiesData)
    }

    override suspend fun signInStudent(
        username: String,
        password: String
    ): Response<Student> {
        return apiService.signInStudent(SignInRequest(username,password))
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
        passing_year: String,
        profile_image_url:String,
    ): Response<Student> {
        return apiService.signUpStudent(SignUpRequest(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year,profile_image_url))
    }

    override suspend fun signOutStudent(): Response<Void> {
        return apiService.signOutStudent()
    }

    override suspend fun forgetPasswordStudent(username: String, email: String): Response<Void> {
        return apiService.forgetPasswordStudent(ForgetPasswordRequest(username,email))
    }

    override suspend fun resetPasswordStudent(
        email: String,
        password: String,
        otp: String
    ): Response<Void> {
        return apiService.resetPasswordStudent(ResetPasswordRequest(email,password,otp))
    }

    override suspend fun updateDataStudent(
        cookies: String,
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
    ): Response<Student> {
        return apiService.updateDataStudent(cookies, UpdateStudentDataRequest(name,registration_no,dob,mobile_no,year,department,division,passing_year,profile_image_url),studentId)
    }

    override suspend fun updatePasswordStudent(
        cookies: String,
        studentId: String,
        oldPassword: String,
        newPassword: String
    ): Response<Void> {
        return apiService.updatePasswordStudent(cookies, UpdateStudentPasswordRequest(oldPassword,newPassword),studentId)
    }

    override suspend fun registerForEventStudent(
        cookies:String,
        studentId: String,
        eventId: String
    ): Response<Void> {
        return apiService.registerForEventStudent(cookies,RegisterEventStudentRequest(studentId, eventId))
    }

    override suspend fun downloadEventCertificateStudent(
        cookies: String,
        studentId: String,
        eventId: String
    ): Response<ResponseBody> {
        return apiService.downloadEventCertificateStudent(cookies,
            DownloadEventCertificateStudentRequest(
                studentId,
                eventId
            )
        )
    }

    override suspend fun getEventsRegisteredStudent(
        cookiesData: String,
        studentId: String
    ): Response<List<Event>> {
        return apiService.getEventsRegisteredStudent(cookiesData,studentId)
    }

    override suspend fun signInAdmin(username: String, password: String): Response<Admin> {
        return apiService.signInAdmin(SignInRequest(username,password))
    }

    override suspend fun registerAdminBySuperAdmin(
        cookiesData: String,
        username: String,
        email: String,
        password: String,
        name: String
    ): Response<Void> {
        return apiService.registerAdminBySuperAdmin(cookiesData,SignUpRequestAdmin(username, email, password, name))
    }

    override suspend fun signOutAdmin(): Response<Void> {
        return apiService.signOutAdmin()
    }

    override suspend fun getEventsCreatedAdmin(
        cookiesData: String,
        adminId: String
    ): Response<List<Event>> {
        return apiService.getEventsCreatedAdmin(cookiesData,adminId)
    }

    override suspend fun createEventsAdmin(
        cookiesData: String,
        adminId: String,
        title: String,
        type: String,
        description: String,
        location: String,
        event_link: String,
        start: String,
        end: String,
        department: String,
        organizer: String,
        longitude: String,
        latitude: String
    ): Response<Event> {
        return apiService.createEventAdmin(cookiesData,adminId, CreateEventRequestAdmin( title, type, description, location, event_link, start, end, department, organizer, longitude, latitude))
    }

    override suspend fun getEventsReportAdmin(
        cookiesData: String,
        eventId: String
    ): Response<List<Student>> {
        return apiService.getEventsReportAdmin(cookiesData,eventId)
    }

    override suspend fun updatePasswordAdmin(
        cookies: String,
        adminId: String,
        oldPassword: String,
        newPassword: String
    ) : Response<Void> {
        return apiService.updatePasswordAdmin(cookies, UpdateStudentPasswordRequest(oldPassword, newPassword),adminId)
    }

    override suspend fun uploadProfilePicture(userId: String, imageUri: Uri) : ResultData<String> {
        return try {
            val storageReference = FirebaseStorage.getInstance().reference.child("$userId.jpg")
            val uploadTask = storageReference.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            ResultData.Success(downloadUrl)
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun updateProfilePictureStudent(
        cookiesData: String,
        studentId: String,
        url: String
    ): Response<String> {
        return apiService.updateProfilePictureStudent(cookiesData,
            UpdateUserProfilePictureRequest(studentId,url)
        )
    }

    override suspend fun updateProfilePictureAdmin(
        cookiesData: String,
        adminId: String,
        url: String
    ): Response<String> {
        return apiService.updateProfilePictureAdmin(cookiesData,UpdateUserProfilePictureRequest(adminId,url))
    }

    override suspend fun forgetPasswordAdmin(username: String, email: String): Response<Void> {
        return apiService.forgetPasswordAdmin(ForgetPasswordRequest(username,email))
    }

    override suspend fun resetPasswordAdmin(
        email: String,
        password: String,
        otp: String
    ): Response<Void> {
        return apiService.resetPasswordAdmin(ResetPasswordRequest(email,password,otp))
    }
}