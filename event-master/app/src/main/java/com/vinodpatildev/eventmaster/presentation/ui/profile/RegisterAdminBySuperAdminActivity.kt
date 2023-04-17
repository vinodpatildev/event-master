package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityRegisterAdminBySuperAdminBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity

@AndroidEntryPoint
class RegisterAdminBySuperAdminActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterAdminBySuperAdminBinding
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel

    private var TIME_OUT:Long = 3000

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAdminBySuperAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ViewModel::class.java)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering admin....")
        progressDialog.setCancelable(false)


        binding.btnCreateAdmin.setOnClickListener {
            if (binding.etUsername.text?.trim().toString().isEmpty()) {
                binding.etUsername.error = "Please Enter username"
                binding.etUsername.requestFocus()
            } else if (binding.etEmail.text?.trim().toString().isEmpty()) {
                binding.etEmail.error = "Please enter your email"
                binding.etEmail.requestFocus()
            } else if (binding.etPassword.text?.trim().toString()
                    .isEmpty() || binding.etPassword.text?.trim().toString().length < 6
            ) {
                binding.etPassword.error = "Please enter password of atleast 6 length"
                binding.etPassword.requestFocus()
            } else if (binding.etName.text?.trim().toString().isEmpty()) {
                binding.etName.error = "Please Enter your name"
                binding.etName.requestFocus()
            } else {
                Log.d("registration", "")
                progressDialog.show()
                val userUserName = binding.etUsername.text?.trim().toString()
                val userEmail = binding.etEmail.text?.trim().toString()
                val userPassword = binding.etPassword.text?.trim().toString()
                val userName = binding.etName.text?.trim().toString()
                //TODO :Upload image
                val profile_image_url = ""

                viewModel.registerAdminBySuperAdmin(userUserName,userEmail,userPassword,userName)
            }
        }

        viewModel.registerAdminBySuperAdminResultAdmin.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    Log.i("success","success")
                    progressDialog.hide()
                    response.data?.let {
                        Snackbar.make(binding.root,"Admin created successfully.",Snackbar.LENGTH_LONG).show()
                        Handler().postDelayed({
                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()
                        },TIME_OUT)
                        finish()
                    }
                }
                is Resource.Loading->{
                    Log.i("success","loading")
                    progressDialog.hide()
                }
                is Resource.Error->{
                    Log.i("success","error")
                    progressDialog.hide()
                    response.message?.let{
                        Snackbar.make(binding.root,"Error Occured:$it",Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}