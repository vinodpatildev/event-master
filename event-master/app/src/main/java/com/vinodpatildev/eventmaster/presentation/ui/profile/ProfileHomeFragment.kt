package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentProfileBinding
import com.vinodpatildev.eventmaster.databinding.FragmentProfileHomeBinding
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileHomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : FragmentProfileHomeBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Signing Our....")
        progressDialog.setCancelable(false)

        val student = viewModel.studentData

        if(student != null){
            binding.tvStudentDepartmentValue.text = student.department
            binding.tvStudentDivisionValue.text = student.division
            binding.tvStudentDobValue.text = student.dob
            binding.tvStudentEmailValue.text = student.email
            binding.tvStudentMobileValue.text = student.mobile
            binding.tvStudentNameValue.text = student.name
            binding.tvStudentYearValue.text = student.year
            binding.tvStudentRegistrationNoValue.text = student.registration_no
            binding.tvStudentUserNameValue.text = student.username
            binding.tvStudentPassingYearValue.text = student.passing_year
        }

        viewModel.signOutResult.observe(requireParentFragment().requireActivity(),androidx.lifecycle.Observer { response ->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
                        viewModel.onAuthSignOut()
                        startActivity(Intent(requireContext(), SignInActivity::class.java))
                        requireParentFragment().requireActivity().finish()
                    }
                }
                is Resource.Loading->{
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        Toast.makeText(requireContext(),"Error Occured:$it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemProfileUpdateData -> {
                val action = ProfileHomeFragmentDirections.actionProfileHomeFragmentToProfileUpdateDataFragment()
                navController.navigate(action)
                return true
            }
            R.id.menuItemProfileUpdatePassword-> {
                val action = ProfileHomeFragmentDirections.actionProfileHomeFragmentToProfileUpdatePasswordFragment()
                navController.navigate(action)
                return true
            }
            R.id.menuItemProfileSignOut -> {
                viewModel.signOutStudent()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Profile")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}