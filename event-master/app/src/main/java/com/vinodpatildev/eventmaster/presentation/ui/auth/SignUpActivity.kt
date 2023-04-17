package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.vinodpatildev.eventmaster.databinding.ActivitySignUpBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : ActivitySignUpBinding

    private lateinit var progressDialog: ProgressDialog

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering....")
        progressDialog.setCancelable(false)

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInUi()
        }
        updateDateInUi()

        binding.etDob.inputType = InputType.TYPE_NULL
        binding.etDob.setOnClickListener {
            DatePickerDialog(
                this@SignUpActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val yearOptions = resources.getStringArray(R.array.batch_year_options)
        val yearArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, yearOptions)
        binding.actvYear.setAdapter(yearArrayAdapter)

        val departmentOptions = resources.getStringArray(R.array.department_options)
        val departmentArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, departmentOptions)
        binding.actvDepartment.setAdapter(departmentArrayAdapter)

        val divisionOptions = resources.getStringArray(R.array.division_options)
        val divisionArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, divisionOptions)
        binding.actvDivision.setAdapter(divisionArrayAdapter)

        val passingYearOptions = resources.getStringArray(R.array.passing_year_options)
        val passingYearArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, passingYearOptions)
        binding.actvPassingYear.setAdapter(passingYearArrayAdapter)

        viewModel.signUpResultStudent.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    Log.i("success","success")
                    progressDialog.hide()
                    response.data?.let {
//                        Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                        viewModel.onAuthCompletedSaveToDatastore(
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
                            it.data.passing_year,
                            it.data.profile_image_url
                        )
//                        startActivity(Intent(this@SignUpActivity,MainActivity::class.java))
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
                        Toast.makeText(this,"Error Occured:$it",Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        binding.btnUpdate.setOnClickListener {
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
            }else if (binding.etRegistrationNo.text?.trim().toString().isEmpty()) {
                binding.etRegistrationNo.error = "Please Enter your Registration No."
                binding.etRegistrationNo.requestFocus()
            }else if (binding.etDob.text?.trim().toString().isEmpty()) {
                binding.etDob.error = "Please Enter your date of birth"
                binding.etDob.requestFocus()
            } else if (binding.etMobile.text?.trim().toString().isEmpty() || binding.etMobile.text?.trim()
                    .toString().length < 10 || binding.etMobile.text?.trim().toString().length > 10
            ) {
                binding.etMobile.error = "Please enter a valid mobile number"
                binding.etMobile.requestFocus()
            }else if (binding.actvYear.text.trim().toString().isEmpty()) {
                binding.actvYear.error = "Please Enter your current year"
                binding.actvYear.requestFocus()
            }else if (binding.actvDepartment.text.trim().toString().isEmpty()) {
                binding.actvDepartment.error = "Please Enter your department"
                binding.actvDepartment.requestFocus()
            }else if (binding.actvDivision.text.trim().toString().isEmpty()) {
                binding.actvDivision.error = "Please Enter your division"
                binding.actvDivision.requestFocus()
            }else if (binding.actvPassingYear.text.trim().toString().isEmpty()) {
                binding.actvPassingYear.error = "Please Enter your passing year"
                binding.actvPassingYear.requestFocus()
            } else if (!binding.checkStatement.isChecked) {
                binding.checkStatement.error = "Please accept all the conditions"
                return@setOnClickListener
            } else {
                Log.d("registration", "It runs")
                progressDialog.show()
                val userUserName = binding.etUsername.text?.trim().toString()
                val userEmail = binding.etEmail.text?.trim().toString()
                val userPassword = binding.etPassword.text?.trim().toString()
                val userName = binding.etName.text?.trim().toString()
                val userRegistrationNo = binding.etRegistrationNo.text?.trim().toString()
                val userDob = binding.etDob.text?.trim().toString()
                val userMobileNumber = binding.etMobile.text?.trim().toString()
                val userYear = binding.actvYear.text.trim().toString()
                val userDepartment = binding.actvDepartment.text.trim().toString()
                val userDivision = binding.actvDivision.text.trim().toString()
                val userPassingYear = binding.actvPassingYear.text.trim().toString()
                //TODO :Upload image
                val profile_image_url = ""

                viewModel.signUpStudent(userUserName,userEmail,userPassword,userName,userRegistrationNo,userDob,userMobileNumber,userYear,userDepartment,userDivision,userPassingYear,profile_image_url)
            }
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun updateDateInUi(){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDob.setText(sdf.format(cal.time).toString())
    }
}