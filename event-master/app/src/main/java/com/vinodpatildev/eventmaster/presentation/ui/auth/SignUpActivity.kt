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
import com.vinodpatildev.eventmaster.presentation.MainActivity

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel

    private lateinit var username: EditText
    private lateinit var profileImage: ImageView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var registration_no: EditText
    private lateinit var dob:EditText
    private lateinit var mobileNumber: EditText
    private lateinit var year: AutoCompleteTextView
    private lateinit var department: AutoCompleteTextView
    private lateinit var division: AutoCompleteTextView
    private lateinit var passing_year:AutoCompleteTextView
    private lateinit var checkBox: CheckBox
    private lateinit var register: Button
    private lateinit var txtLogin: TextView
    private lateinit var progressDialog: ProgressDialog

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        username = findViewById(R.id.et_username)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        name = findViewById(R.id.et_name)
        registration_no = findViewById(R.id.et_registration_no)
        dob = findViewById(R.id.et_dob)
        mobileNumber = findViewById(R.id.et_mobile)
        year = findViewById(R.id.actv_year)
        department = findViewById(R.id.actv_department)
        division = findViewById(R.id.actv_division)
        passing_year = findViewById(R.id.actv_passing_year)
        checkBox = findViewById(R.id.check_statement)
        register = findViewById(R.id.btn_update)
        txtLogin = findViewById(R.id.txt_login)
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

        dob.inputType = InputType.TYPE_NULL
        dob.setOnClickListener {
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
        year.setAdapter(yearArrayAdapter)

        val departmentOptions = resources.getStringArray(R.array.department_options)
        val departmentArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, departmentOptions)
        department.setAdapter(departmentArrayAdapter)

        val divisionOptions = resources.getStringArray(R.array.division_options)
        val divisionArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, divisionOptions)
        division.setAdapter(divisionArrayAdapter)

        val passingYearOptions = resources.getStringArray(R.array.passing_year_options)
        val passingYearArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, passingYearOptions)
        passing_year.setAdapter(passingYearArrayAdapter)

        viewModel.signUpResult.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    Log.i("success","success")
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
                        startActivity(Intent(this@SignUpActivity,MainActivity::class.java))
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

        register.setOnClickListener {
            if (username.text.trim().toString().isEmpty()) {
                username.error = "Please Enter username"
                username.requestFocus()
            } else if (email.text.trim().toString().isEmpty()) {
                email.error = "Please enter your email"
                email.requestFocus()
            } else if (password.text.trim().toString()
                    .isEmpty() || password.text.trim().toString().length < 6
            ) {
                password.error = "Please enter password of atleast 6 length"
                password.requestFocus()
            } else if (name.text.trim().toString().isEmpty()) {
                name.error = "Please Enter your name"
                name.requestFocus()
            }else if (registration_no.text.trim().toString().isEmpty()) {
                registration_no.error = "Please Enter your Registration No."
                registration_no.requestFocus()
            }else if (dob.text.trim().toString().isEmpty()) {
                dob.error = "Please Enter your date of birth"
                dob.requestFocus()
            } else if (mobileNumber.text.trim().toString().isEmpty() || mobileNumber.text.trim()
                    .toString().length < 10 || mobileNumber.text.trim().toString().length > 10
            ) {
                mobileNumber.error = "Please enter a valid mobile number"
                mobileNumber.requestFocus()
            }else if (year.text.trim().toString().isEmpty()) {
                year.error = "Please Enter your current year"
                year.requestFocus()
            }else if (department.text.trim().toString().isEmpty()) {
                department.error = "Please Enter your department"
                department.requestFocus()
            }else if (division.text.trim().toString().isEmpty()) {
                division.error = "Please Enter your division"
                division.requestFocus()
            }else if (passing_year.text.trim().toString().isEmpty()) {
                passing_year.error = "Please Enter your passing year"
                passing_year.requestFocus()
            } else if (!checkBox.isChecked) {
                checkBox.error = "Please accept all the conditions"
                return@setOnClickListener
            } else {
                Log.d("registration", "It runs")
                progressDialog.show()
                val userUserName = username.text.trim().toString()
                val userEmail = email.text.trim().toString()
                val userPassword = password.text.trim().toString()
                val userName = name.text.trim().toString()
                val userRegistrationNo = registration_no.text.trim().toString()
                val userDob = dob.text.trim().toString()
                val userMobileNumber = mobileNumber.text.trim().toString()
                val userYear = year.text.trim().toString()
                val userDepartment = department.text.trim().toString()
                val userDivision = division.text.trim().toString()
                val userPassingYear = passing_year.text.trim().toString()

                viewModel.signUpStudent(userUserName,userEmail,userPassword,userName,userRegistrationNo,userDob,userMobileNumber,userYear,userDepartment,userDivision,userPassingYear)
            }
        }

        txtLogin.setOnClickListener {
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
        dob.setText(sdf.format(cal.time).toString())

    }
}