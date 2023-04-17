package com.vinodpatildev.eventmaster.presentation.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentStudentSignInBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class StudentSignInFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private var binding : FragmentStudentSignInBinding? = null
    private lateinit var parentActivity:AppCompatActivity
    companion object {
        fun newInstance() = StudentSignInFragment()
    }
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        binding = FragmentStudentSignInBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity = requireActivity() as SignInActivity
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Signing as Student....")
        progressDialog.setCancelable(false)

        checkUserStatus()

        binding?.switchStudentAdmin?.setOnClickListener {
            viewModel.user = "admin"
            viewModel.userUpdate("admin")
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.SignInContainer, AdminSignInFragment.newInstance())
                .commitNow()
        }

        viewModel.viewModelUpdated.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    binding?.btnLogin?.isEnabled = true
                }
                is Resource.Loading->{
                    binding?.btnLogin?.isEnabled = false
                }
                else -> {
                    binding?.btnLogin?.isEnabled = true
                }
            }
        })

        binding?.btnLogin?.setOnClickListener {
            if (binding?.etUsernameLogin?.text?.trim().toString().isEmpty()) {
                binding?.etUsernameLogin?.error = "Please enter a valid username"
                binding?.etUsernameLogin?.requestFocus()
            } else if (binding?.etPasswordLogin?.text?.trim().toString().isEmpty()) {
                binding?.etPasswordLogin?.error = "Please enter a valid password"
                binding?.etPasswordLogin?.requestFocus()
            } else {
                val username = binding?.etUsernameLogin?.text?.trim().toString()
                val password = binding?.etPasswordLogin?.text?.trim().toString()

                viewModel.signInStudent(username,password)
            }
        }
        binding?.txtRegister?.setOnClickListener {
            startActivity(Intent(parentActivity, SignUpActivity::class.java))
//            parentActivity.finish()
        }
        binding?.txtForgetPassword?.setOnClickListener {
            startActivity(Intent(parentActivity, ForgetPasswordActivity::class.java))
//            parentActivity.finish()
        }

        viewModel.signInResultStudent.observe(viewLifecycleOwner, Observer { response->
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
                        startActivity(Intent(parentActivity,MainActivity::class.java))
                        parentActivity.finish()
                    }
                }
                is Resource.Loading->{
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        binding?.let { it1 -> Snackbar.make(it1.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show() }
                    }
                }
                else -> {}
            }
        })
    }

    private fun checkUserStatus() {
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.userStatus.collect{
                withContext(Dispatchers.Main){
                    if(it == true){
                        startActivity(Intent(parentActivity,MainActivity::class.java))
                        parentActivity.finish()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.userUpdate("student")
    }
}