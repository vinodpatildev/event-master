package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityForgetPasswordBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityForgetPasswordBinding

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in....")
        progressDialog.setCancelable(false)

        viewModel.viewModelUpdated.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    binding.btnGetPasswordEmail.isEnabled = true
                }
                is Resource.Loading->{
                    binding.btnGetPasswordEmail.isEnabled = false
                }
                else -> {
                    binding.btnGetPasswordEmail.isEnabled = true
                }
            }
        })

        viewModel.forgetPasswordResultStudent.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
                        startActivity(Intent(this@ForgetPasswordActivity, OtpActivity::class.java))
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
        viewModel.forgetPasswordResultAdmin.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
                        startActivity(Intent(this@ForgetPasswordActivity, OtpActivity::class.java))
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
        binding.btnGetPasswordEmail.setOnClickListener {
            if (binding.etUsername.text?.trim().toString().isEmpty()) {
                binding.etUsername.error = "Please enter a valid username."
                binding.etUsername.requestFocus()
            } else if (binding.etEmail.text?.trim().toString().isEmpty()) {
                binding.etEmail.error = "Please enter a valid email."
                binding.etEmail.requestFocus()
            } else {
                val username = binding.etUsername.text?.trim().toString()
                val password = binding.etEmail.text?.trim().toString()

                when(viewModel.user){
                    "admin" -> {
                        Snackbar.make(binding.root,"admin requsting otp.",Snackbar.LENGTH_LONG).show()
                        viewModel.forgetPasswordAdmin(username,password)
                    }
                    "student" -> {
                        Snackbar.make(binding.root,"student requsting otp.",Snackbar.LENGTH_LONG).show()
                        viewModel.forgetPasswordStudent(username,password)
                    }
                }
            }
        }
        binding.txtSignin.setOnClickListener {
            finish()
        }
    }
}