package com.vinodpatildev.eventmaster.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

class ViewModel(
    private val app: Application,
    private val preferencesManager: PreferencesManager,
    private val getEventsUseCase : GetEventsUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val loginStudentUseCase : SignInStudentUseCase,
    private val signUpStudentUseCase : SignUpStudentUseCase,
    private val signOutStudentUseCase: SignOutStudentUseCase,
    private val updateStudentDataUseCase: UpdateStudentDataUseCase,
    private val updateStudentPasswordUseCase: UpdateStudentPasswordUseCase
):AndroidViewModel(app) {
    var studentData :Student? = null
    var cookiesData :String? = null

    val preferencesFlow = preferencesManager.preferencesFlow

    val userData: MutableStateFlow<ApiResponse<Student>> = MutableStateFlow(ApiResponse(Student(0,"","","","","","","","","","","",""),""))
    val userStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userType: MutableStateFlow<String> = MutableStateFlow("student")

    val signInResult: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val signUpResult: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val signOutResult : MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updateStudentDataResult: MutableLiveData<Resource<ApiResponse<Student>>> = MutableLiveData()
    val updateStudentPasswordResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val eventList: MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    val notificationList: MutableLiveData<Resource<List<Notification>>> = MutableLiveData()

    init {
        viewModelScope.launch {
            preferencesFlow.collect{
                preferences ->
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
                    preferences.year
                )
                userData.value = ApiResponse(student, preferences.cookies )
                studentData = student
                cookiesData = preferences.cookies
                userStatus.value = preferences.isUserLoggedIn
                userType.value = preferences.userType
            }
        }
    }

    fun signInStudent(username: String, password: String) = viewModelScope.launch(Dispatchers.IO){
        signInResult.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = loginStudentUseCase.execute(username,password)
                signInResult.postValue(apiResult)
            }else{
                signInResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            signInResult.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun signUpStudent(username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String) = viewModelScope.launch(Dispatchers.IO){
        eventList.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signUpStudentUseCase.execute(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year)
                signUpResult.postValue(apiResult)
            }else{
                signUpResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            signUpResult.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun signOutStudent() = viewModelScope.launch(Dispatchers.IO){
        signOutResult.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = signOutStudentUseCase.execute()
                signOutResult.postValue(apiResult)
            }else{
                signOutResult.postValue(Resource.Success(true))
            }
        }catch (e:Exception){
            signOutResult.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun getEvents() = viewModelScope.launch(Dispatchers.IO){
        eventList.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = getEventsUseCase.execute(cookiesData!!)
                apiResult.data?.get(0)?.let { Log.i("testtesttest2", it.toString()) }
                eventList.postValue(apiResult)
            }else{
                eventList.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            eventList.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun getNotifications() = viewModelScope.launch(Dispatchers.IO){
        notificationList.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = getNotificationsUseCase.execute(cookiesData!!)
                notificationList.postValue(apiResult)
            }else{
                notificationList.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            notificationList.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun updateStudentData(name: String,
                          registration_no: String,
                          dob: String,
                          mobile_no: String,
                          year: String,
                          department: String,
                          division: String,
                          passing_year: String) = viewModelScope.launch(Dispatchers.IO){
        updateStudentDataResult.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = updateStudentDataUseCase.execute(cookiesData!!,
                    studentData!!._id,
                    name,
                    registration_no,
                    dob,
                    mobile_no,
                    year,
                    department,
                    division,
                    passing_year)
                updateStudentDataResult.postValue(apiResult)
            }else{
                updateStudentDataResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            updateStudentDataResult.postValue(Resource.Error(e.message.toString()))
        }
    }
    fun updateStudentPassword(oldPassword:String, newPassword:String) = viewModelScope.launch(Dispatchers.IO){
        updateStudentPasswordResult.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)){
                val apiResult = updateStudentPasswordUseCase.execute(cookiesData!!,
                    studentData!!._id,
                    oldPassword,
                    newPassword
                )
                updateStudentPasswordResult.postValue(apiResult)
            }else{
                updateStudentPasswordResult.postValue(Resource.Error("Internet is not available."))
            }
        }catch (e:Exception){
            updateStudentPasswordResult.postValue(Resource.Error(e.message.toString()))
        }
    }

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

    fun onAuthCompleted(cookies:String,isUserLoggedIn:Boolean,userType:String, userId:String, username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String) = viewModelScope.launch {
        preferencesManager.updateUserStudentData(cookies,true,userType, userId, username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year)
    }
    fun onAuthSignOut() = viewModelScope.launch {
        preferencesManager.updateUserStudentData("",false,"","","","","","","","","","","","","")
    }

}