package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentProfileHomeBinding
import com.vinodpatildev.eventmaster.databinding.FragmentProfileUpdateDataBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdateDataFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : FragmentProfileUpdateDataBinding
    private lateinit var navController: NavController

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileUpdateDataBinding.inflate(inflater,container,false)
        requireParentFragment().requireActivity().onBackPressedDispatcher.addCallback(requireParentFragment().requireActivity()) {
            navController.popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Updating....")
        progressDialog.setCancelable(false)

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

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInUi()
        }

        binding.etDob.inputType = InputType.TYPE_NULL
        binding.etDob.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val yearOptions = resources.getStringArray(R.array.batch_year_options)
        val yearArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, yearOptions)
        binding.actvYear.setAdapter(yearArrayAdapter)

        val departmentOptions = resources.getStringArray(R.array.department_options)
        val departmentArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, departmentOptions)
        binding.actvDepartment.setAdapter(departmentArrayAdapter)

        val divisionOptions = resources.getStringArray(R.array.division_options)
        val divisionArrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, divisionOptions)
        binding.actvDivision.setAdapter(divisionArrayAdapter)

        val passingYearOptions = resources.getStringArray(R.array.passing_year_options)
        val passingYearArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, passingYearOptions)
        binding.actvPassingYear.setAdapter(passingYearArrayAdapter)

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

                viewModel.updateStudentData(userName,userRegistrationNo,userDob,userMobileNumber,userYear,userDepartment,userDivision,userPassingYear)

                viewModel.updateStudentDataResult.observe(requireParentFragment().requireActivity(),
                    androidx.lifecycle.Observer { response ->
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
                                    navController.navigate(ProfileUpdateDataFragmentDirections.actionProfileUpdateDataFragmentToProfileHomeFragment())
                                }
                            }
                            is Resource.Loading->{
                                progressDialog.show()
                            }
                            is Resource.Error->{
                                progressDialog.hide()
                                response.message?.let{
                                    Toast.makeText(requireContext(),"Error Occured:$it",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    })
            }
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Update Data")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun updateDateInUi(){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDob.setText(sdf.format(cal.time).toString())
    }
}