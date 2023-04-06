package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityOtpBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : ActivityOtpBinding
    private lateinit var progressDialog: ProgressDialog
    private var TIME_OUT:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in....")
        progressDialog.setCancelable(false)

        viewModel.resetPasswordResult.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    Snackbar.make(binding.root,"Password updated successfully.",Snackbar.LENGTH_LONG).show()
                    response.data?.let {
                        Handler().postDelayed({
                            startActivity(Intent(this@OtpActivity, SignInActivity::class.java))
                            finish()
                        },TIME_OUT)

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
        binding.btnReset.setOnClickListener {
            if (binding.etEmail.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                binding.etEmail.error = "Please enter email."
                binding.etEmail.requestFocus()
            } else if (binding.etNewPassword.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                binding.etNewPassword.error = "Please enter new password of atleast 6 length"
                binding.etNewPassword.requestFocus()
            } else if (binding.etNewPasswordConfirm.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                binding.etNewPasswordConfirm.error = "Please enter password of atleast 6 length"
                binding.etNewPasswordConfirm.requestFocus()
            }else if (binding.etNewPassword.text?.trim().toString().isEmpty() != binding.etNewPasswordConfirm.text?.trim().toString().isEmpty()) {
                binding.etNewPasswordConfirm.error = "Confirmation password does not match."
                binding.etNewPasswordConfirm.requestFocus()
            }else{
                val email = binding.etEmail.text?.trim().toString()
                val newPassword = binding.etNewPassword.text?.trim().toString()
                val otp = binding.etOtp.text?.trim().toString()

                viewModel.resetStudentPassword(email, newPassword, otp)
            }
        }
    }
}