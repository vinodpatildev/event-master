package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentProfileUpdateDataBinding
import com.vinodpatildev.eventmaster.databinding.FragmentProfileUpdatePasswordBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.ui.notification.NotificationFragment
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdatePasswordFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding : FragmentProfileUpdatePasswordBinding
    private lateinit var navController: NavController

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileUpdatePasswordBinding.inflate(inflater,container,false)
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

                viewModel.updateStudentPassword(oldPassword, newPassword)
            }
        }
        viewModel.updateStudentPasswordResult.observe(requireParentFragment().requireActivity(), androidx.lifecycle.Observer { response ->
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
        viewModel.signOutResult.observe(requireParentFragment().requireActivity(),androidx.lifecycle.Observer { response ->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    Snackbar.make(binding.clFragmentProfileUpdatePassword,"successfully changed the password.",Snackbar.LENGTH_LONG).show()
                    response.data?.let {
                        viewModel.onAuthSignOut()
                        startActivity(Intent(requireContext(),SignInActivity::class.java))
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Update Password")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}