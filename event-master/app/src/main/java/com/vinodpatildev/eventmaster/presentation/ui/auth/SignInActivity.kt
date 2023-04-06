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
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.databinding.ActivityForgetPasswordBinding
import com.vinodpatildev.eventmaster.databinding.ActivitySignInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel

    private lateinit var binding: ActivitySignInBinding


    var userStatus: Boolean = false

    private lateinit var usernameLogin: TextView
    private lateinit var passwordLogin: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var txtForgetPassword: TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in....")
        progressDialog.setCancelable(false)

        checkUserStatus()

        viewModel.signInResult.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
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
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        Snackbar.make(binding.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })


        binding.btnLogin.setOnClickListener {
            if (binding.etUsernameLogin.text?.trim().toString().isEmpty()) {
                binding.etUsernameLogin.error = "Please enter a valid username"
                binding.etUsernameLogin.requestFocus()
            } else if (binding.etPasswordLogin.text?.trim().toString().isEmpty()) {
                binding.etPasswordLogin.error = "Please enter a valid password"
                binding.etPasswordLogin.requestFocus()
            } else {
                val username = binding.etUsernameLogin.text?.trim().toString()
                val password = binding.etPasswordLogin.text?.trim().toString()

                viewModel.signInStudent(username,password)
            }
        }
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        binding.txtForgetPassword.setOnClickListener {
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