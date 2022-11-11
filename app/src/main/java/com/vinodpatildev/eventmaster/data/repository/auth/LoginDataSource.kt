package com.vinodpatildev.eventmaster.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.vinodpatildev.eventmaster.data.Result
import com.vinodpatildev.eventmaster.data.model.LoggedInUser
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(val auth: FirebaseAuth) {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        if(auth != null){
            try {
                // TODO: handle loggedInUser authentication
                auth.signInWithEmailAndPassword(username,password).await()
                if(auth.currentUser != null){
                    val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "sucess")
                    return Result.Success(fakeUser)
                }
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "failure")
                return Result.Success(fakeUser)
            } catch (e: Throwable) {
                return Result.Error(IOException("Error logging in", e))
            }
        }else{
            return Result.Error(IOException("Auth instance null."))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}