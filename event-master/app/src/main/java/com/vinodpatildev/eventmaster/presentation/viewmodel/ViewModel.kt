package com.vinodpatildev.eventmaster.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinodpatildev.eventmaster.data.model.*
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ViewModel(
    private val app: Application,
    private val preferencesManager: PreferencesManager,
    private val getEventsUseCase : GetEventsUseCase,
    private val reloadEventsUseCase : ReloadEventsUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val reloadNotificationsUseCase: ReloadNotificationsUseCase,
    private val loginStudentUseCase : SignInStudentUseCase,
    private val signUpStudentUseCase : SignUpStudentUseCase,
    private val signOutStudentUseCase: SignOutStudentUseCase,
    private val updateDataStudentUseCase: UpdateDataStudentUseCase,
    private val updatePasswordStudentUseCase: UpdatePasswordStudentUseCase,
    private val updatePasswordAdminUseCase: UpdatePasswordAdminUseCase,
    private val forgetPasswordStudentUseCase: ForgetPasswordStudentUseCase,
    private val resetPasswordStudentUseCase: ResetPasswordStudentUseCase,
    private val registerEventStudentUseCase: RegisterEventStudentUseCase,
    private val downloadEventCertificateStudentUseCase: DownloadEventCertificateStudentUseCase,
    private val getEventsRegisteredStudentUseCase: GetEventsRegisteredStudentUseCase,
    private val reloadEventsRegisteredStudentUseCase: ReloadEventsRegisteredStudentUseCase,
    private val signInAdminUseCase : SignInAdminUseCase,
    private val registerAdminBySuperAdminUseCase: RegisterAdminBySuperAdminUseCase,
    private val signOutAdminUseCase: SignOutAdminUseCase,
    private val getEventsCreatedAdminUseCase: GetEventsCreatedAdminUseCase,
    private val reloadEventsCreatedAdminUseCase: ReloadEventsCreatedAdminUseCase,
    private val createEventByAdminUseCase: CreateEventByAdminUseCase,
    private val getEventReportAdminUseCase: GetEventReportAdminUseCase,
    private val uploadProfilePictureUseCase:UploadProfilePictureUseCase,
    private val updateProfilePictureStudentUseCase:UpdateProfilePictureStudentUseCase,
    private val updateProfilePictureAdminUseCase:UpdateProfilePictureAdminUseCase,
    private val forgetPasswordAdminUseCase : ForgetPasswordAdminUseCase,
    private val resetPasswordAdminUseCase : ResetPasswordAdminUseCase
    ):AndroidViewModel(app) {
    private val TAG = "EVENTMASTERDEBUGLOGS"
    var user : String = ""
    var cookiesData :String? = null
    val preferencesFlow = preferencesManager.preferencesFlow
    var modified : String = ""
    val modifiedLiveData  :MutableLiveData<String> = MutableLiveData("")
    val viewModelUpdated  :MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val userType: MutableStateFlow<String> = MutableStateFlow("student")
    val userStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uploadProfilePictureUseCaseResult:MutableLiveData<Resource<String>> = MutableLiveData()
    val updateProfilePictureUseCaseResult:MutableLiveData<Resource<String>> = MutableLiveData()
    // Student Attributes
    var studentData :Student? = null
    val userDataStudent: MutableStateFlow<ApiResponse<Student>> = MutableStateFlow(ApiResponse<Student>(Student(0,"","","","","","","","","","","","",false,""),""))

    val signInResultStudent: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val signUpResultStudent: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val signOutResultStudent : MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updateDataResultStudent: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val updatePasswordResultStudent: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val eventList: MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    val eventRegisteredListStudent : MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    val notificationList: MutableLiveData<Resource<List<Notification>>> = MutableLiveData()

    val forgetPasswordResultStudent:MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val resetPasswordResultStudent:MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val registerEventResultStudent:MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val downloadEventCertificateResultStudent : MutableLiveData<Resource<String>> = MutableLiveData()
    var geofenceEventMonitoring : Boolean = false
    val geofenceEventMonitoringLiveData : MutableLiveData<Boolean> = MutableLiveData(false)
    // Student Attributes

    // Admin Attributes
    var adminData :Admin? = null
    val userDataAdmin: MutableStateFlow<ApiResponse<Admin>> = MutableStateFlow(ApiResponse(Admin(0,"","","","","",""),""))

    val signInResultAdmin: MutableLiveData<Resource<ApiResponse<Admin>>> = MutableLiveData()
    val registerAdminBySuperAdminResultAdmin: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val signOutResultAdmin : MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updateDataResultAdmin: MutableLiveData<Resource<ApiResponse<Admin>>> = MutableLiveData()
    val updatePasswordResultAdmin: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val eventsCreatedListAdmin : MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    val createEventAdminResult : MutableLiveData<Resource<Event>> = MutableLiveData()

    val eventReportAdminResult : MutableLiveData<Resource<List<Student>>> = MutableLiveData()

    val forgetPasswordResultAdmin:MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val resetPasswordResultAdmin:MutableLiveData<Resource<Boolean>> = MutableLiveData()

    // Admin Attributes

    init {
        viewModelUpdated.postValue(Resource.Loading())
        viewModelScope.launch {
            preferencesFlow.collect{
                preferences ->
                if(preferences.userType == "admin"){
                    user = "admin"
                    val admin = Admin(
                        0,
                        preferences.userId,
                        preferences.email,
                        preferences.password,
                        preferences.username,
                        preferences.name,
                        preferences.profile_image_url
                    )
                    userDataAdmin.value = ApiResponse(admin, preferences.cookies)
                    adminData = admin
                }
                else if(preferences.userType == "student"){
                    user = "student"
                    val student = Student(
                        0,
                        preferences.userId,
                        preferences.department,
                        preferences.division,
                        preferences.dob,
                        preferences.email,
                        preferences.mobile_no,
                        preferences.name,
                        preferences.passing_year,
                        preferences.password,
                        preferences.registration_no,
                        preferences.username,
                        preferences.year,
                        false,
                        preferences.profile_image_url
                    )
                    userDataStudent.value = ApiResponse(student, preferences.cookies )
                    studentData = student
                }
                cookiesData = preferences.cookies
                userStatus.value = preferences.isUserLoggedIn
                userType.value = preferences.userType
                modified = preferences.modified
                viewModelUpdated.postValue(Resource.Success(true))
            }
        }
    }

    fun getEvents() = viewModelScope.launch(Dispatchers.IO){
        eventList.postValue(Resource.Loading())
        try {
            val apiResult = getEventsUseCase.execute(cookiesData!!)
            eventList.postValue(apiResult)
        }catch (e:Exception){
            eventList.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun reloadEvents() = viewModelScope.launch(Dispatchers.IO){
        eventList.postValue(Resource.Loading())
        try {
            val apiResult = reloadEventsUseCase.execute(cookiesData!!)
            eventList.postValue(apiResult)
        }catch (e:Exception){
            eventList.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun getNotifications() = viewModelScope.launch(Dispatchers.IO){
        notificationList.postValue(Resource.Loading())
        try {
            val apiResult = getNotificationsUseCase.execute(cookiesData!!)
            notificationList.postValue(apiResult)
        }catch (e:Exception){
            notificationList.postValue(Resource.Error(e.message.toString()))
        }
    }

    // Student Attributes
    fun signInStudent(username: String, password: String) = viewModelScope.launch(Dispatchers.IO){
        signInResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = loginStudentUseCase.execute(username,password)
                signInResultStudent.postValue(apiResult)
            }else{
                signInResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            signInResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String,profile_image_url:String) = viewModelScope.launch(Dispatchers.IO){
        eventList.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signUpStudentUseCase.execute(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year,profile_image_url)
                signUpResultStudent.postValue(apiResult)
            }else{
                signUpResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            signUpResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun signOutStudent() = viewModelScope.launch(Dispatchers.IO){
        signOutResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signOutStudentUseCase.execute()
                signOutResultStudent.postValue(apiResult)
            }else{
                signOutResultStudent.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            signOutResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun forgetPasswordStudent(username: String, email: String) = viewModelScope.launch(Dispatchers.IO){
        forgetPasswordResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = forgetPasswordStudentUseCase.execute(username, email)
                forgetPasswordResultStudent.postValue(apiResult)
            }else{
                forgetPasswordResultStudent.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            forgetPasswordResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun forgetPasswordAdmin(username: String, email: String) = viewModelScope.launch(Dispatchers.IO){
        forgetPasswordResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = forgetPasswordAdminUseCase.execute(username, email)
                forgetPasswordResultAdmin.postValue(apiResult)
            }else{
                forgetPasswordResultAdmin.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            forgetPasswordResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun resetPasswordAdmin(email: String, password: String, otp: String)  = viewModelScope.launch(Dispatchers.IO){
        resetPasswordResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = resetPasswordAdminUseCase.execute(email,password,otp)
                resetPasswordResultAdmin.postValue(apiResult)
            }else{
                resetPasswordResultAdmin.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            resetPasswordResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }


    fun resetPasswordStudent(email: String, password: String, otp:String) = viewModelScope.launch(Dispatchers.IO){
        resetPasswordResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = resetPasswordStudentUseCase.execute(email, password, otp)
                resetPasswordResultStudent.postValue(apiResult)
            }else{
                resetPasswordResultStudent.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            resetPasswordResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }


    fun getEventsRegisteredStudent() = viewModelScope.launch(Dispatchers.IO){
        eventRegisteredListStudent.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(app)){
                val apiResult =
                    studentData?.let { getEventsRegisteredStudentUseCase.execute(cookiesData!!, it._id) }
                eventRegisteredListStudent.postValue(apiResult!!)
            }else{
                eventRegisteredListStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            eventRegisteredListStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun updateDataStudent(name: String,
                          registration_no: String,
                          dob: String,
                          mobile_no: String,
                          year: String,
                          department: String,
                          division: String,
                          passing_year: String,
                          profile_image_url: String) = viewModelScope.launch(Dispatchers.IO){
        updateDataResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = updateDataStudentUseCase.execute(cookiesData!!,
                    studentData!!._id,
                    name,
                    registration_no,
                    dob,
                    mobile_no,
                    year,
                    department,
                    division,
                    passing_year,profile_image_url)
                updateDataResultStudent.postValue(apiResult)
            }else{
                updateDataResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            updateDataResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun updatePasswordStudent(oldPassword:String, newPassword:String) = viewModelScope.launch(Dispatchers.IO){
        updatePasswordResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = updatePasswordStudentUseCase.execute(cookiesData!!,
                    studentData!!._id,
                    oldPassword,
                    newPassword
                )
                updatePasswordResultStudent.postValue(apiResult)
            }else{
                updatePasswordResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            updatePasswordResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun updatePasswordAdmin(oldPassword:String, newPassword:String) = viewModelScope.launch(Dispatchers.IO){
        updatePasswordResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = updatePasswordAdminUseCase.execute(cookiesData!!,
                    adminData!!._id,
                    oldPassword,
                    newPassword
                )
                updatePasswordResultAdmin.postValue(apiResult)
            }else{
                updatePasswordResultAdmin.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            updatePasswordResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun registerEventStudent(eventId:String) = viewModelScope.launch(Dispatchers.IO) {
        registerEventResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = studentData?.let { registerEventStudentUseCase.execute(cookiesData!!, it._id, eventId) }
                registerEventResultStudent.postValue(apiResult!!)
            }else{
                registerEventResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            registerEventResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun downloadEventCertificateStudent(context:Context, eventId:String) = viewModelScope.launch(Dispatchers.IO) {
        downloadEventCertificateResultStudent.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = studentData?.let { downloadEventCertificateStudentUseCase.execute(context, cookiesData!!, it._id, eventId ) }
                downloadEventCertificateResultStudent.postValue(apiResult!!)
            }else{
                downloadEventCertificateResultStudent.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            downloadEventCertificateResultStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    // Student Attributes


    private fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun onAuthCompletedSaveToDatastore(cookies:String, isUserLoggedIn:Boolean, userType:String, userId:String, username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String,profile_image_url:String) = viewModelScope.launch {
        preferencesManager.updateUserStudentData(cookies,true,userType, userId, username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year,profile_image_url)
    }
    fun onAuthSignOutSaveToDatastore() = viewModelScope.launch {
        preferencesManager.updateUserStudentData("",false,"","","","","","","","","","","","","","")
    }
    fun onModified() = viewModelScope.launch {
        val key = System.currentTimeMillis().toString()
        modified = key
        preferencesManager.onModified(key)
    }
    fun userUpdate(user:String) = viewModelScope.launch {
        preferencesManager.currentUser(user)
    }

    // Admin Attributes

    fun signInAdmin(username: String, password: String) = viewModelScope.launch(Dispatchers.IO){
        signInResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signInAdminUseCase.execute(username,password)
                signInResultAdmin.postValue(apiResult)
            }else{
                signInResultAdmin.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            signInResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun registerAdminBySuperAdmin(username: String, email: String, password: String, name:String) = viewModelScope.launch(Dispatchers.IO){
        registerAdminBySuperAdminResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult =  registerAdminBySuperAdminUseCase.execute(cookiesData!!,username,email,password,name)
                registerAdminBySuperAdminResultAdmin.postValue(apiResult)
            }else{
                registerAdminBySuperAdminResultAdmin.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            registerAdminBySuperAdminResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun signOutAdmin() = viewModelScope.launch(Dispatchers.IO){
        signOutResultAdmin.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signOutAdminUseCase.execute()
                signOutResultAdmin.postValue(apiResult)
            }else{
                signOutResultAdmin.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            signOutResultAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun getEventsCreatedAdmin() = viewModelScope.launch(Dispatchers.IO){
        eventsCreatedListAdmin.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(app)){
                val apiResult = adminData?.let { getEventsCreatedAdminUseCase.execute(cookiesData!!, it._id) }
                eventsCreatedListAdmin.postValue(apiResult!!)
            }else{
                eventsCreatedListAdmin.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            eventsCreatedListAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun createEventAdmin(title : String, type : String, description : String, location : String, event_link : String, start: String, end: String, department : String, organizer : String, longitude : String, latitude : String) = viewModelScope.launch(Dispatchers.IO){
        createEventAdminResult.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(app)){
                val apiResult = adminData?.let { createEventByAdminUseCase.execute(cookiesData!!, adminData!!._id, title, type, description, location, event_link, start, end, department, organizer, longitude, latitude) }
                createEventAdminResult.postValue(apiResult!!)
            }else{
                createEventAdminResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            createEventAdminResult.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun getEventReportAdmin(eventId:String) = viewModelScope.launch(Dispatchers.IO){
        eventReportAdminResult.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(app)){
                val apiResult = adminData?.let { getEventReportAdminUseCase.execute(cookiesData!!, eventId) }
                eventReportAdminResult.postValue(apiResult!!)
            }else{
                eventReportAdminResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            eventReportAdminResult.postValue(Resource.Error(e.message.toString()))
        }
    }


    fun reloadNotifications() = viewModelScope.launch(Dispatchers.IO){
        notificationList.postValue(Resource.Loading())
        try {
            val apiResult = reloadNotificationsUseCase.execute(cookiesData!!)
            notificationList.postValue(apiResult)
        }catch (e:Exception){
            notificationList.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun reloadCreatedEvents(adminId:String) = viewModelScope.launch(Dispatchers.IO){
        eventsCreatedListAdmin.postValue(Resource.Loading())
        try {
            val apiResult = reloadEventsCreatedAdminUseCase.execute(cookiesData!!,adminId)
            eventsCreatedListAdmin.postValue(apiResult)
        }catch (e:Exception){
            eventsCreatedListAdmin.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun reloadRegisteredEvents(studentId:String) = viewModelScope.launch(Dispatchers.IO){
        eventRegisteredListStudent.postValue(Resource.Loading())
        try {
            val apiResult = reloadEventsRegisteredStudentUseCase.execute(cookiesData!!,studentId)
            eventRegisteredListStudent.postValue(apiResult)
        }catch (e:Exception){
            eventRegisteredListStudent.postValue(Resource.Error(e.message.toString()))
        }
    }

    // Admin Attributes
    fun uploadProfilePicture( imageUri: Uri) = viewModelScope.launch(Dispatchers.IO){
        uploadProfilePictureUseCaseResult.postValue(Resource.Loading())
        try {
            val userId = if(user=="admin") adminData?._id else studentData?._id
            val apiResult = uploadProfilePictureUseCase.execute(userId!!,imageUri)
            uploadProfilePictureUseCaseResult.postValue(apiResult!!)
        }catch (e:Exception){
            uploadProfilePictureUseCaseResult.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun updateProfilePictureUser( url: String) = viewModelScope.launch(Dispatchers.IO){
        updateProfilePictureUseCaseResult.postValue(Resource.Loading())
        try {
            val userId = if(user=="admin") adminData?._id else studentData?._id
            when(user){
                "admin" -> {
                    val apiResult = updateProfilePictureAdminUseCase.execute(cookiesData!!,userId!!,url)
                    updateProfilePictureUseCaseResult.postValue(apiResult!!)
                }
                "student" -> {
                    val apiResult = updateProfilePictureStudentUseCase.execute(cookiesData!!,userId!!,url)
                    updateProfilePictureUseCaseResult.postValue(apiResult!!)
                }
                else -> {
                    updateProfilePictureUseCaseResult.postValue(Resource.Error("user not initialized."))
                }
            }
        }catch (e:Exception){
            updateProfilePictureUseCaseResult.postValue(Resource.Error(e.message.toString()))
        }
    }
}