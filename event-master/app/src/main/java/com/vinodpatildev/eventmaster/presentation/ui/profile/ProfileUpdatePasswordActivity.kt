package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityProfileUpdatePasswordBinding
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdatePasswordActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : ActivityProfileUpdatePasswordBinding

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        supportActionBar?.setTitle("Update Password")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Updating Password....")
        progressDialog.setCancelable(false)

        when(viewModel.user){
            "admin" -> {
                binding.btnUpdate.setOnClickListener {
                    if (binding.etOldPassword.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etOldPassword.error = "Please Enter old password"
                        binding.etOldPassword.requestFocus()
                    } else if (binding.etNewPassword.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etNewPassword.error = "Please enter new password"
                        binding.etNewPassword.requestFocus()
                    } else if (binding.etNewPasswordConfirm.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etNewPasswordConfirm.error = "Please enter password of atleast 6 length"
                        binding.etNewPasswordConfirm.requestFocus()
                    }else if (binding.etNewPassword.text?.trim().toString().isEmpty() != binding.etNewPasswordConfirm.text?.trim().toString().isEmpty()) {
                        binding.etNewPasswordConfirm.error = "Confirmation password does not match."
                        binding.etNewPasswordConfirm.requestFocus()
                    }else{
                        val oldPassword = binding.etOldPassword.text?.trim().toString()
                        val newPassword = binding.etNewPassword.text?.trim().toString()

                        viewModel.updatePasswordAdmin(oldPassword, newPassword)
                    }
                }
                viewModel.updatePasswordResultAdmin.observe(this, androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            Snackbar.make(binding.clFragmentProfileUpdatePassword,"successfully changed the password.",Snackbar.LENGTH_LONG).show()
                            response.data?.let {
                                viewModel.signOutStudent()
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
                viewModel.signOutResultAdmin.observe(this,androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            Snackbar.make(binding.clFragmentProfileUpdatePassword,"successfully changed the password.",
                                Snackbar.LENGTH_LONG).show()
                            response.data?.let {
                                viewModel.onAuthSignOutSaveToDatastore()
//                                startActivity(Intent(this, SignInActivity::class.java))
                                finish()
                            }
                        }
                        is Resource.Loading->{
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
            }
            "student" -> {
                binding.btnUpdate.setOnClickListener {
                    if (binding.etOldPassword.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etOldPassword.error = "Please Enter old password"
                        binding.etOldPassword.requestFocus()
                    } else if (binding.etNewPassword.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etNewPassword.error = "Please enter new password"
                        binding.etNewPassword.requestFocus()
                    } else if (binding.etNewPasswordConfirm.text?.trim().toString().isEmpty() || binding.etNewPasswordConfirm.text?.trim().toString().length < 6) {
                        binding.etNewPasswordConfirm.error = "Please enter password of atleast 6 length"
                        binding.etNewPasswordConfirm.requestFocus()
                    }else if (binding.etNewPassword.text?.trim().toString().isEmpty() != binding.etNewPasswordConfirm.text?.trim().toString().isEmpty()) {
                        binding.etNewPasswordConfirm.error = "Confirmation password does not match."
                        binding.etNewPasswordConfirm.requestFocus()
                    }else{
                        val oldPassword = binding.etOldPassword.text?.trim().toString()
                        val newPassword = binding.etNewPassword.text?.trim().toString()

                        viewModel.updatePasswordStudent(oldPassword, newPassword)
                    }
                }
                viewModel.updatePasswordResultStudent.observe(this, androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            Snackbar.make(binding.clFragmentProfileUpdatePassword,"successfully changed the password.",
                                Snackbar.LENGTH_LONG).show()
                            response.data?.let {
                                viewModel.signOutStudent()
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
                viewModel.signOutResultStudent.observe(this,androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            Snackbar.make(binding.clFragmentProfileUpdatePassword,"successfully changed the password.",
                                Snackbar.LENGTH_LONG).show()
                            response.data?.let {
                                viewModel.onAuthSignOutSaveToDatastore()
//                                startActivity(Intent(this, SignInActivity::class.java))
                                finish()
                            }
                        }
                        is Resource.Loading->{
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
            }
        }
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