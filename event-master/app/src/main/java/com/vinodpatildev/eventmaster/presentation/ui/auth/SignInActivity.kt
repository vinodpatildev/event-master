package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel

    var userStatus: Boolean = false

    private lateinit var usernameLogin: TextView
    private lateinit var passwordLogin: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var txtForgetPassword: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)


        usernameLogin = findViewById(R.id.et_username_login)
        passwordLogin = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        txtRegister = findViewById(R.id.txt_register)
        txtForgetPassword = findViewById(R.id.txt_forget_password)
        progressBar = findViewById(R.id.progressLogin)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in....")
        progressDialog.setCancelable(false)

        checkUserStatus()

        viewModel.signInResult.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
//                        Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                        viewModel.onAuthCompleted(
                            it.cookies,
                            true,
                            "student",
                            it.data._id,
                            it.data.username,
                            it.data.email,
                            it.data.password,
                            it.data.name,
                            it.data.registration_no,
                            it.data.dob,
                            it.data.mobile,
                            it.data.year,
                            it.data.department,
                            it.data.division,
                            it.data.passing_year
                        )
                        startActivity(Intent(this@SignInActivity,MainActivity::class.java))
                        finish()
                    }
                }
                is Resource.Loading->{
                    Log.i("success","loading")
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        Toast.makeText(this,"Error Occured:$it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })


        btnLogin.setOnClickListener {
            if (usernameLogin.text.trim().toString().isEmpty()) {
                usernameLogin.error = "Please enter a valid username"
                usernameLogin.requestFocus()
            } else if (passwordLogin.text.trim().toString().isEmpty()) {
                passwordLogin.error = "Please enter a valid password"
                passwordLogin.requestFocus()
            } else {
                progressBar.visibility = View.VISIBLE
                val username = usernameLogin.text.trim().toString()
                val password = passwordLogin.text.trim().toString()

                // Sign In the user
                // Do UI Changes
                viewModel.signInStudent(username,password)
            }
        }
        txtRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        txtForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }
    }

    private fun checkUserStatus() {
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.userStatus.collect{
                withContext(Dispatchers.Main){
                    if(it == true){
                        startActivity(Intent(this@SignInActivity,MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}