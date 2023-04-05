package com.vinodpatildev.eventmaster.data.model

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

//username: String, email: String, password: String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String

private const val TAG = "PreferencesManager"

data class FilterPreferences(
    val cookies:String,
    val isUserLoggedIn:Boolean,
    val userType:String,
    val userId:String,
    val username:String,
    val email:String,
    val password:String,
    val name:String,
    val registration_no:String,
    val dob:String,
    val mobile_no:String,
    val year:String,
    val department:String,
    val division:String,
    val passing_year:String,
)
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.createDataStore("user_preferences")
    val preferencesFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e(TAG,"Error reading preferences.",exception)
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val cookies = preferences[PreferencesKeys.COOKIES] ?: ""
            val isUserLoggedIn = preferences[PreferencesKeys.IS_USER_LOGGED_IN] ?: false
            val userType = preferences[PreferencesKeys.USER_TYPE]?:""
            val userId = preferences[PreferencesKeys.USER_ID]?:""
            val username = preferences[PreferencesKeys.USERNAME]?:""
            val email = preferences[PreferencesKeys.EMAIL]?:""
            val password = preferences[PreferencesKeys.PASSWORD]?:""
            val name = preferences[PreferencesKeys.NAME]?:""
            val registration_no = preferences[PreferencesKeys.REGISTRATION_NO]?:""
            val dob = preferences[PreferencesKeys.DOB]?:""
            val mobile_no = preferences[PreferencesKeys.MOBILE_NO]?:""
            val year = preferences[PreferencesKeys.YEAR]?:""
            val department = preferences[PreferencesKeys.DEPARTMENT]?:""
            val division = preferences[PreferencesKeys.DIVISION]?:""
            val passing_year = preferences[PreferencesKeys.PASSING_YEAR]?:""

            FilterPreferences(
                cookies,
                isUserLoggedIn,
                userType,
                userId,
                username,
                email,
                password,
                name,
                registration_no,
                dob,
                mobile_no,
                year,
                department,
                division,
                passing_year
            )
        }
    suspend fun updateUserStudentData(cookies: String,
                                      isUserLoggedIn:Boolean,
                                      userType:String,
                                      userId:String,
                                      username:String,
                                      email:String,
                                      password:String,
                                      name:String,
                                      registration_no:String,
                                      dob:String,
                                      mobile_no:String,
                                      year:String,
                                      department:String,
                                      division:String,
                                      passing_year:String){
        dataStore.edit { preferences->
            preferences[PreferencesKeys.COOKIES] = cookies
            preferences[PreferencesKeys.IS_USER_LOGGED_IN] = isUserLoggedIn
            preferences[PreferencesKeys.USER_TYPE] = userType
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.PASSWORD] = password
            preferences[PreferencesKeys.NAME] = name
            preferences[PreferencesKeys.REGISTRATION_NO] = registration_no
            preferences[PreferencesKeys.DOB] = dob
            preferences[PreferencesKeys.MOBILE_NO] = mobile_no
            preferences[PreferencesKeys.YEAR] = year
            preferences[PreferencesKeys.DEPARTMENT] = department
            preferences[PreferencesKeys.DIVISION] = division
            preferences[PreferencesKeys.PASSING_YEAR] = passing_year
        }
    }
    object PreferencesKeys{
        val COOKIES = preferencesKey<String>("cookies")
        val IS_USER_LOGGED_IN = preferencesKey<Boolean>("is_user_logged_in")
        val USER_TYPE = preferencesKey<String>("user_type")
        val USER_ID = preferencesKey<String>("user_id")
        val USERNAME = preferencesKey<String>("username")
        val EMAIL = preferencesKey<String>("email")
        val PASSWORD = preferencesKey<String>("password")
        val NAME = preferencesKey<String>("name")
        val REGISTRATION_NO = preferencesKey<String>("registration_no")
        val DOB = preferencesKey<String>("dob")
        val MOBILE_NO = preferencesKey<String>("mobile_no")
        val YEAR = preferencesKey<String>("year")
        val DEPARTMENT = preferencesKey<String>("department")
        val DIVISION = preferencesKey<String>("division")
        val PASSING_YEAR = preferencesKey<String>("passing_year")
    }
}
