package com.vinodpatildev.eventmasterauthenticator

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.vinodpatildev.eventmasterauthenticator.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private val TAG = "event-master-auth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener{
            registerUser()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile(binding.etProfile.text.toString(),"https://firebasestorage.googleapis.com/v0/b/event-master-6ba84.appspot.com/o/3.jpg?alt=media&token=6258ce21-17c1-42ff-9cb1-571c8a68418e")
        }
        binding.btnUpdateEmail.setOnClickListener {
            updateEmail(binding.etEmail.toString())
        }
        binding.btnUpdatePassword.setOnClickListener {
            updatePassword(binding.etPassword.toString())
        }

        binding.btnLogout.setOnClickListener{
            if(auth.currentUser != null){
                auth.signOut()
                checkLoggedInState()
            }
        }

    }

    private fun registerUser(){
        showProgressBar()
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){
                        binding.etEmailRegister.text.clear()
                        binding.etPasswordRegister.text.clear()
                        checkLoggedInState()
                        hideProgressBar()
                    }
                }catch (exc : Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,exc.message,Toast.LENGTH_LONG).show()
                    }
                    hideProgressBar()
                }
            }
        }
        checkLoggedInState()
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) { // not logged in
            binding.tvLoggingStatus.text = "Logged Out"
        } else {
            binding.tvLoggingStatus.text = "Logged In"
        }
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun getUserProfile() {
        val user = auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

             val emailVerified = user.isEmailVerified

            val uid = user.uid
        }
    }

    private fun updateProfile(name:String,imageUri:String) {
        showProgressBar()
        val user = auth.currentUser
        if(name.isNotEmpty() && Patterns.WEB_URL.matcher(imageUri).matches()){
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = Uri.parse(imageUri)
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                        binding.etProfile.text.clear()
                        checkLoggedInState()
                        hideProgressBar()
                    }
                }

        }else{
            Toast.makeText(this,"wrong profile data",Toast.LENGTH_LONG).show()
            hideProgressBar()
        }
    }

    private fun updateEmail(email:String) {
        showProgressBar()
        val user = auth.currentUser

        if(email.contains("@ghrcem.raisoni.net") && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            user!!.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User email address updated.")
                        Toast.makeText(this,"updated email",Toast.LENGTH_LONG).show()
                    }
                    binding.etEmail.text.clear()
                    checkLoggedInState()
                    hideProgressBar()
                }

        }else{
            Toast.makeText(this,"wrong email",Toast.LENGTH_LONG).show()
            hideProgressBar()
        }
    }

    private fun updatePassword(password:String) {
        showProgressBar()
        val user = auth.currentUser

        if(password.length > 5){
            user!!.updatePassword(password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User password updated.")
                        Toast.makeText(this,"updated password",Toast.LENGTH_LONG).show()
                        binding.etPassword.text.clear()
                        checkLoggedInState()
                        hideProgressBar()
                    }
                }
        }else{
            Toast.makeText(this,"wrong password",Toast.LENGTH_LONG).show()
            hideProgressBar()
        }

    }


}