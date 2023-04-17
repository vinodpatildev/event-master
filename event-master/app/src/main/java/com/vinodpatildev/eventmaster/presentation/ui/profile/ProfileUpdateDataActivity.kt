package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityProfileUpdateDataBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdateDataActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : ActivityProfileUpdateDataBinding
    private lateinit var navController: NavController

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)


        supportActionBar?.setTitle("Update Data")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Updating....")
        progressDialog.setCancelable(false)

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInUi()
        }

        binding.etDob.inputType = InputType.TYPE_NULL
        binding.etDob.setOnClickListener {
            DatePickerDialog(
                this,
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

        when(viewModel.user){
            "admin" -> {
                val admin = viewModel.adminData
                if(admin != null){
                    binding.apply{
                        registrationNoLayout.visibility = View.GONE
                        dobLayout.visibility = View.GONE
                        mobileLayout.visibility = View.GONE
                        yearLayout.visibility = View.GONE
                        departmentLayout.visibility = View.GONE
                        divisionLayout.visibility = View.GONE
                        passingYearLayout.visibility = View.GONE

                        etName.setText(admin.name)
                    }
                }
            }
            "student" -> {
                val student = viewModel.studentData

                if(student != null){
                    binding.etName.setText(student.name)
                    binding.etRegistrationNo.setText(student.registration_no)
                    binding.etDob.setText(student.dob)
                    binding.etMobile.setText(student.mobile)
                    binding.actvYear.setText(student.year)
                    binding.actvDepartment.setText(student.department)
                    binding.actvDivision.setText(student.division)
                    binding.actvPassingYear.setText(student.passing_year)
                }
            }
        }
        binding.btnUpdate.setOnClickListener {
            if (binding.etName.text?.trim().toString().isEmpty()) {
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
            }  else {
                progressDialog.show()

                val userName = binding.etName.text?.trim().toString()
                val userRegistrationNo = binding.etRegistrationNo.text?.trim().toString()
                val userDob = binding.etDob.text?.trim().toString()
                val userMobileNumber = binding.etMobile.text?.trim().toString()
                val userYear = binding.actvYear.text.trim().toString()
                val userDepartment = binding.actvDepartment.text.trim().toString()
                val userDivision = binding.actvDivision.text.trim().toString()
                val userPassingYear = binding.actvPassingYear.text.trim().toString()
                //TODO : Image upload
                val profile_image_url = ""

                when(viewModel.user){
                    "admin" -> {
//                        viewModel.updateDataAdmin(userName,userRegistrationNo,userDob,userMobileNumber,userYear,userDepartment,userDivision,userPassingYear,profile_image_url)
//                        viewModel.updateDataResultAdmin.observe(this, androidx.lifecycle.Observer { response ->
//                            when(response){
//                                is Resource.Success->{
//                                    progressDialog.hide()
//                                    response.data?.let {
//                                        viewModel.onAuthCompletedSaveToDatastore(
//                                            it.cookies,
//                                            true,
//                                            "student",
//                                            it.data._id,
//                                            it.data.username,
//                                            it.data.email,
//                                            it.data.password,
//                                            it.data.name,
//                                            "",
//                                            "",
//                                            "",
//                                            "",
//                                            "",
//                                            "",
//                                            "",
//                                            "",
//                                            )
//                        finish()
//                                    }
//                                }
//                                is Resource.Loading->{
//                                    progressDialog.show()
//                                }
//                                is Resource.Error->{
//                                    progressDialog.hide()
//                                    response.message?.let{
//                                        Toast.makeText(this,"Error Occured:$it",
//                                            Toast.LENGTH_LONG).show()
//                                    }
//                                }
//                            }
//                        })
                    }
                    "student" -> {
                        viewModel.updateDataStudent(userName,userRegistrationNo,userDob,userMobileNumber,userYear,userDepartment,userDivision,userPassingYear,profile_image_url)
                        viewModel.updateDataResultStudent.observe(this, androidx.lifecycle.Observer { response ->
                            when(response){
                                is Resource.Success->{
                                    progressDialog.hide()
                                    response.data?.let {
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
                                        finish()
                                    }
                                }
                                is Resource.Loading->{
                                    progressDialog.show()
                                }
                                is Resource.Error->{
                                    progressDialog.hide()
                                    response.message?.let{
                                        Toast.makeText(this,"Error Occured:$it",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        })
                    }
                }

            }
        }
    }

    private fun updateDateInUi(){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDob.setText(sdf.format(cal.time).toString())
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