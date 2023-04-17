package com.vinodpatildev.eventmaster.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import com.vinodpatildev.eventmaster.data.model.*
import com.vinodpatildev.eventmaster.data.repository.datasource.CacheDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.LocalDataSource
import com.vinodpatildev.eventmaster.data.repository.datasource.RemoteDataSource
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.data.util.ResultData
import com.vinodpatildev.eventmaster.domain.repository.Repository
import java.io.File

class RepositoryImpl(
    private val appContext: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val cacheDataSource: CacheDataSource
): Repository {
    override suspend fun getEvents( cookiesData:String ): Resource<List<Event>> {
        return getEventsFromCaches(cookiesData)
    }

    override suspend fun reloadEvents(cookiesData: String): Resource<List<Event>> {
        if(isNetworkAvailable(appContext)){
            var response = remoteDataSource.getEvents(cookiesData)
            if(response.isSuccessful){
                response.body()?.let{result->
                    val eventList = Resource.Success(result)
                    localDataSource.saveAllEventsToDB(eventList.data!!)
                    cacheDataSource.saveEventsToCache(eventList.data!!)
                    return eventList
                }
            }else{
                return Resource.Error(response.message())
            }
        }
        return Resource.Error("Internet is not available.")
    }

    private suspend fun getEventsFromCaches(cookiesData: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = cacheDataSource.getEventsFromCache()
            eventList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_3",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsFromDB(cookiesData)
            cacheDataSource.saveEventsToCache(eventList.data!!)
        }
        return eventList
    }
    private suspend fun getEventsFromDB(cookiesData: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = localDataSource.getAllEventsFromDB()
            eventList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_2",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsFromAPI(cookiesData)
            localDataSource.saveAllEventsToDB(eventList.data!!)
        }
        return eventList
    }
    private suspend fun getEventsFromAPI(cookiesData: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            if(isNetworkAvailable(appContext)){
                var response = remoteDataSource.getEvents(cookiesData)
                if(response.isSuccessful){
                    response.body()?.let{result->
                        eventList = Resource.Success(result)
                    }
                }else{
                    eventList = Resource.Error(response.message())
                }
            }else{
                eventList = Resource.Error("Internet is not available.")
            }

        } catch (exception : Exception){
            Log.i("fuck_mylog_1",exception.message.toString())
            eventList = Resource.Error(exception.message.toString())
        }
        return eventList
    }

    override suspend fun getNotifications(cookiesData:String, ): Resource<List<Notification>> {
        return getNotificationsFromCaches(cookiesData)
    }

    override suspend fun reloadNotifications(cookiesData: String): Resource<List<Notification>> {
        if(isNetworkAvailable(appContext)){
            var response = remoteDataSource.getNotifications(cookiesData)
            if(response.isSuccessful){
                response.body()?.let{result->
                    val notificationList = Resource.Success(result)
                    localDataSource.saveAllNotificationsToDB(notificationList.data!!)
                    cacheDataSource.saveNotificationsToCache(notificationList.data!!)
                    return notificationList;
                }
            }else{
                return Resource.Error(response.message())
            }
        }
        return Resource.Error("Internet is not available.")
    }

    private suspend fun getNotificationsFromCaches(cookiesData: String):Resource<List<Notification>>{
        lateinit var notificationList : Resource<List<Notification>>
        try {
            val result = cacheDataSource.getNotificationsFromCache()
            notificationList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_3",exception.message.toString())
        }
        if(notificationList.data?.size!! <= 0){
            notificationList = getNotificationsFromDB(cookiesData)
            cacheDataSource.saveNotificationsToCache(notificationList.data!!)
        }
        return notificationList
    }
    private suspend fun getNotificationsFromDB(cookiesData: String):Resource<List<Notification>>{
        lateinit var notificationList : Resource<List<Notification>>
        try {
            val result = localDataSource.getAllNotificationsFromDB()
            notificationList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_2",exception.message.toString())
        }
        if(notificationList.data?.size!! <= 0){
            notificationList = getNotificationsFromAPI(cookiesData)
            localDataSource.saveAllNotificationsToDB(notificationList.data!!)
        }
        return notificationList
    }
    private suspend fun getNotificationsFromAPI(cookiesData: String):Resource<List<Notification>>{
        lateinit var notificationList : Resource<List<Notification>>
        try {
            if(isNetworkAvailable(appContext)){
                var response = remoteDataSource.getNotifications(cookiesData)
                if(response.isSuccessful){
                    response.body()?.let{result->
                        notificationList = Resource.Success(result)
                    }
                }else{
                    notificationList = Resource.Error(response.message())
                }
            }else{
                notificationList = Resource.Error("Internet is not available.")
            }
        } catch (exception : Exception){
            Log.i("fuck_mylog_1",exception.message.toString())
            notificationList = Resource.Error(exception.message.toString())
        }
        return notificationList
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

        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(ApiResponse<Student>(result,sidValue!!))
            }
        }
        return Resource.Error(response.message())
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
        profile_image_url: String
    ): Resource<ApiResponse<Student>> {
        val response = remoteDataSource.signUpStudent(username, email, password, name, registration_no, dob, mobile_no, year, department, division, passing_year, profile_image_url)
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(ApiResponse<Student>(result,sidValue!!))
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun singOutStudent(): Resource<Boolean> {
        val response = remoteDataSource.signOutStudent()
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun forgetPasswordStudent(username: String, email: String): Resource<Boolean> {
        val response = remoteDataSource.forgetPasswordStudent(username,email)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun resetPasswordStudent(
        email: String,
        password: String,
        otp: String
    ): Resource<Boolean> {
        val response = remoteDataSource.resetPasswordStudent(email,password,otp)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
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
    ): Resource<ApiResponse<Student>> {
        val response = remoteDataSource.updateDataStudent(cookies,
            studentId,
            name,
            registration_no,
            dob,
            mobile_no,
            year,
            department,
            division,
            passing_year,
            profile_image_url
        )
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)
        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(ApiResponse<Student>(result,sidValue!!))
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun updatePasswordStudent(
        cookies: String,
        studentId: String,
        oldPassword: String,
        newPassword: String
    ): Resource<Boolean> {
        val response = remoteDataSource.updatePasswordStudent(
            cookies,
            studentId,
            oldPassword,
            newPassword
        )
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun updatePasswordAdmin(
        cookies: String,
        adminId: String,
        oldPassword: String,
        newPassword: String
    ): Resource<Boolean> {
        val response = remoteDataSource.updatePasswordAdmin(
            cookies, adminId, oldPassword, newPassword
        )
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun registerForEventStudent(
        cookies: String,
        studentId: String,
        eventId: String
    ): Resource<Boolean> {
        val response = remoteDataSource.registerForEventStudent(cookies,studentId, eventId)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun downloadEventCertificateStudent(
        context: Context,
        cookies: String,
        studentId: String,
        eventId: String
    ): Resource<String> {
        val response = remoteDataSource.downloadEventCertificateStudent(cookies, studentId, eventId)

        if (response.isSuccessful) {
            val responseBody = response.body()
            val contentDisposition = response.headers()["Content-Disposition"]
            val filenameRegex = Regex("filename=\"(.*?)\"")
            val filenameMatch = filenameRegex.find(contentDisposition.orEmpty())
            val filename = filenameMatch?.groupValues?.get(1) ?: "certificate.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            responseBody?.byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
            }
            return Resource.Success(file.absolutePath)
        }
        return Resource.Error(response.message())
    }

    //Todo
    override suspend fun getEventsRegisteredStudent(
        cookiesData: String,
        studentId: String
    ): Resource<List<Event>> {
        return getEventsRegisteredFromCaches(cookiesData,studentId)
    }

    override suspend fun reloadEventsRegisteredStudent(
        cookiesData: String,
        studentId: String
    ): Resource<List<Event>> {
        if(isNetworkAvailable(appContext)){
            var response = remoteDataSource.getEventsRegisteredStudent(cookiesData, studentId)
            if(response.isSuccessful){
                response.body()?.let{result->
                    val eventList = Resource.Success(result)
                    var resultRegisteredEvent : ArrayList<RegisteredEvent> = arrayListOf<RegisteredEvent>()
                    for(event in eventList.data!!){
                        resultRegisteredEvent.add(
                            RegisteredEvent(
                                event.__v,
                                event._id,
                                event.adminId,
                                event.createdAt,
                                event.department,
                                event.description,
                                event.end,
                                event.event_link,
                                event.organizer,
                                event.start,
                                event.state,
                                event.title,
                                event.type,
                                event.updatedAt,
                                event.location,
                                event.longitude,
                                event.latitude
                            )
                        )
                    }

                    localDataSource.saveAllRegisteredEventsToDB(resultRegisteredEvent)
                    cacheDataSource.saveRegisteredEventsToCache(eventList.data!!)
                    return eventList
                }
            }else{
                return Resource.Error(response.message())
            }
        }
        return Resource.Error("Internet is not available.")
    }

    private suspend fun getEventsRegisteredFromCaches(cookiesData: String,studentId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = cacheDataSource.getRegisteredEventsFromCache()
            eventList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_3",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsRegisteredFromDB(cookiesData,studentId)
            cacheDataSource.saveRegisteredEventsToCache(eventList.data!!)
        }
        return eventList
    }
    private suspend fun getEventsRegisteredFromDB(cookiesData: String,studentId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = localDataSource.getAllRegisteredEventsFromDB()
            var resultEvents : ArrayList<Event> = arrayListOf<Event>()
            for(registedEvent in result){
                resultEvents.add(Event(
                    registedEvent.__v,
                    registedEvent._id,
                    registedEvent.adminId,
                    registedEvent.createdAt,
                    registedEvent.department,
                    registedEvent.description,
                    registedEvent.end,
                    registedEvent.event_link,
                    registedEvent.organizer,
                    registedEvent.start,
                    registedEvent.state,
                    registedEvent.title,
                    registedEvent.type,
                    registedEvent.updatedAt,
                    registedEvent.location,
                    registedEvent.longitude,
                    registedEvent.latitude
                )
                )
            }
            eventList = Resource.Success(resultEvents.toList())
        }catch (exception :Exception){
            Log.i("fuck_mylog_2",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsRegisteredFromAPI(cookiesData,studentId)
            var resultRegisteredEvent : ArrayList<RegisteredEvent> = arrayListOf<RegisteredEvent>()
            for(event in eventList.data!!){
                resultRegisteredEvent.add(
                    RegisteredEvent(
                        event.__v,
                        event._id,
                        event.adminId,
                        event.createdAt,
                        event.department,
                        event.description,
                        event.end,
                        event.event_link,
                        event.organizer,
                        event.start,
                        event.state,
                        event.title,
                        event.type,
                        event.updatedAt,
                        event.location,
                        event.longitude,
                        event.latitude
                    )
                )
            }
            localDataSource.saveAllRegisteredEventsToDB(resultRegisteredEvent.toList())
        }
        return eventList
    }
    private suspend fun getEventsRegisteredFromAPI(cookiesData: String,studentId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            if(isNetworkAvailable(appContext)){
                var response = remoteDataSource.getEventsRegisteredStudent(cookiesData, studentId)
                if(response.isSuccessful){
                    response.body()?.let{result->
                        eventList = Resource.Success(result)
                    }
                }else{
                    eventList = Resource.Error(response.message())
                }
            }else{
                eventList = Resource.Error("Internet is not available.")
            }

        } catch (exception : Exception){
            Log.i("fuck_mylog_1",exception.message.toString())
            eventList = Resource.Error(exception.message.toString())
        }
        return eventList
    }


    override suspend fun signInAdmin(
        username: String,
        password: String
    ): Resource<ApiResponse<Admin>> {
        val response = remoteDataSource.signInAdmin(username, password)
        val cookieString = response.headers().get("Set-Cookie").toString()
        val pattern = "([^;]+)".toRegex()
        val matchResult = pattern.find(cookieString)
        val sidValue = matchResult?.groupValues?.get(1)

        if(response.isSuccessful){
            response.body()?.let{result->
                return Resource.Success(ApiResponse<Admin>(result,sidValue!!))
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun registerAdminBySuperAdmin(
        cookiesData: String,
        username: String,
        email: String,
        password: String,
        name: String
    ): Resource<Boolean> {
        val response = remoteDataSource.registerAdminBySuperAdmin(cookiesData, username, email, password, name)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun singOutAdmin(): Resource<Boolean> {
        val response = remoteDataSource.signOutAdmin()
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    //Todo
    override suspend fun getEventsCreatedAdmin(
        cookiesData: String,
        adminId: String
    ): Resource<List<Event>> {
        return getEventsCreatedFromCaches(cookiesData,adminId)
    }

    override suspend fun reloadEventsCreatedAdmin(
        cookiesData: String,
        adminId: String
    ): Resource<List<Event>> {
        if(isNetworkAvailable(appContext)){
            var response = remoteDataSource.getEventsCreatedAdmin(cookiesData,adminId)
            if(response.isSuccessful){
                response.body()?.let{result->
                    val eventList = Resource.Success(result)
                    var resultCreatedEvent : ArrayList<CreatedEvent> = arrayListOf<CreatedEvent>()
                    for(event in eventList.data!!){
                        resultCreatedEvent.add(
                            CreatedEvent(
                                event.__v,
                                event._id,
                                event.adminId,
                                event.createdAt,
                                event.department,
                                event.description,
                                event.end,
                                event.event_link,
                                event.organizer,
                                event.start,
                                event.state,
                                event.title,
                                event.type,
                                event.updatedAt,
                                event.location,
                                event.longitude,
                                event.latitude
                            )
                        )
                    }
                    localDataSource.saveAllCreatedEventsToDB(resultCreatedEvent)
                    cacheDataSource.saveCreatedEventsToCache(eventList.data!!)
                    return eventList
                }
            }else{
                return Resource.Error(response.message())
            }
        }
        return Resource.Error("Internet is not available.")
    }

    private suspend fun getEventsCreatedFromCaches(cookiesData: String,adminId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = cacheDataSource.getCreatedEventsFromCache()
            eventList = Resource.Success(result)
        }catch (exception :Exception){
            Log.i("fuck_mylog_3",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsCreatedFromDB(cookiesData,adminId)
            cacheDataSource.saveCreatedEventsToCache(eventList.data!!)
        }
        return eventList
    }
    private suspend fun getEventsCreatedFromDB(cookiesData: String,adminId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            val result = localDataSource.getAllCreatedEventsFromDB()
            var resultCreatedEvents : ArrayList<Event> = arrayListOf<Event>()
            for(createdEvent in result){
                resultCreatedEvents.add(Event(
                    createdEvent.__v,
                    createdEvent._id,
                    createdEvent.adminId,
                    createdEvent.createdAt,
                    createdEvent.department,
                    createdEvent.description,
                    createdEvent.end,
                    createdEvent.event_link,
                    createdEvent.organizer,
                    createdEvent.start,
                    createdEvent.state,
                    createdEvent.title,
                    createdEvent.type,
                    createdEvent.updatedAt,
                    createdEvent.location,
                    createdEvent.longitude,
                    createdEvent.latitude
                    )
                )
            }
            eventList = Resource.Success(resultCreatedEvents.toList())
        }catch (exception :Exception){
            Log.i("fuck_mylog_2",exception.message.toString())
        }
        if(eventList.data?.size!! <= 0){
            eventList = getEventsCreatedFromAPI(cookiesData,adminId)
            var resultCreatedEvent : ArrayList<CreatedEvent> = arrayListOf<CreatedEvent>()
            for(event in eventList.data!!){
                resultCreatedEvent.add(
                    CreatedEvent(
                    event.__v,
                    event._id,
                    event.adminId,
                    event.createdAt,
                    event.department,
                    event.description,
                    event.end,
                    event.event_link,
                    event.organizer,
                    event.start,
                    event.state,
                    event.title,
                    event.type,
                    event.updatedAt,
                    event.location,
                    event.longitude,
                    event.latitude
                    )
                )
            }
            localDataSource.saveAllCreatedEventsToDB(resultCreatedEvent.toList())
        }
        return eventList
    }
    private suspend fun getEventsCreatedFromAPI(cookiesData: String,adminId: String):Resource<List<Event>>{
        lateinit var eventList : Resource<List<Event>>
        try {
            if(isNetworkAvailable(appContext)){
                var response = remoteDataSource.getEventsCreatedAdmin(cookiesData,adminId)
                if(response.isSuccessful){
                    response.body()?.let{result->
                        eventList = Resource.Success(result)
                    }
                }else{
                    eventList = Resource.Error(response.message())
                }
            }else{
                eventList = Resource.Error("Internet is not available.")
            }

        } catch (exception : Exception){
            Log.i("fuck_mylog_1",exception.message.toString())
            eventList = Resource.Error(exception.message.toString())
        }
        return eventList
    }

    override suspend fun CreateEventAdmin(
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
    ): Resource<Event> {
        val response = remoteDataSource.createEventsAdmin(cookiesData,adminId,title,type,description,location,event_link,start,end,department,organizer, longitude, latitude)
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getEventReportAdmin(
        cookiesData: String,
        eventId: String
    ): Resource<List<Student>> {
        val response = remoteDataSource.getEventsReportAdmin(cookiesData, eventId)
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun uploadProfilePicture(userId: String, imageUri: Uri): Resource<String> {
        val result = remoteDataSource.uploadProfilePicture(userId,imageUri);
        return when (result) {
            is ResultData.Success -> {
                Resource.Success(result.data)
            }
            is ResultData.Error -> {
                Resource.Error(result.exception.message.toString())
            }
            else ->{
                Resource.Error("Could not upload the data")
            }
        }
    }

    override suspend fun updateProfilePictureStudent(
        cookiesData: String,
        studentId: String,
        url: String
    ): Resource<String> {
        val response = remoteDataSource.updateProfilePictureStudent(cookiesData, studentId, url)
        if(response.isSuccessful){
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }

    override suspend fun updateProfilePictureAdmin(
        cookiesData: String,
        adminId: String,
        url: String
    ): Resource<String> {
        val response = remoteDataSource.updateProfilePictureAdmin(cookiesData, adminId, url)
        if(response.isSuccessful){
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }

    override suspend fun forgetPasswordAdmin(username: String, email: String): Resource<Boolean> {
        val response = remoteDataSource.forgetPasswordAdmin(username,email)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
    }

    override suspend fun resetPasswordAdmin(
        email: String,
        password: String,
        otp: String
    ): Resource<Boolean> {
        val response = remoteDataSource.resetPasswordAdmin(email,password,otp)
        if(response.isSuccessful){
            return Resource.Success(true)
        }
        return Resource.Error(response.message())
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
}