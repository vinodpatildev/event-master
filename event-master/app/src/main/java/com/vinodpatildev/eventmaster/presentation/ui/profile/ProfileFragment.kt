package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.FragmentProfileBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var navController: NavController
    private lateinit var binding : FragmentProfileBinding
    companion object {
        fun newInstance() = ProfileFragment()
    }
    private var mainActivity: MainActivity? = null
    private val gallaryLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ){
            result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK){
            val uri: Uri? = result.data?.data
            uri.let {
                viewModel.uploadProfilePicture(it!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Processing....")
        progressDialog.setCancelable(false)

        binding.btnEdit.setOnClickListener {
            choosePhotoFromGallery()
        }
        viewModel.uploadProfilePictureUseCaseResult.observe(requireActivity(),androidx.lifecycle.Observer { response ->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
                        viewModel.updateProfilePictureUser(it)
                    }
                }
                is Resource.Loading->{
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        Toast.makeText(requireContext(),"Error Occured : $it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        viewModel.modifiedLiveData.observe(viewLifecycleOwner, Observer {
            when(viewModel.user){
                "admin" -> {
                    Glide.with(this@ProfileFragment).load(viewModel.adminData?.profile_image_url).signature(
                        ObjectKey(viewModel.modified)
                    ).into(binding.userProfilePhoto)
                }
                "student" -> {
                    Glide.with(this@ProfileFragment).load(viewModel.studentData?.profile_image_url).signature(
                        ObjectKey(viewModel.modified)
                    ).into(binding.userProfilePhoto)
                }
            }
        })
        viewModel.updateProfilePictureUseCaseResult.observe(requireActivity(),androidx.lifecycle.Observer { response ->
            when(response){
                is Resource.Success->{
                    progressDialog.hide()
                    response.data?.let {
                        Toast.makeText(requireContext()," Profile Picture updated successfully. ", Toast.LENGTH_LONG).show()
                        viewModel.onModified()
                    }
                }
                is Resource.Loading->{
                    progressDialog.show()
                }
                is Resource.Error->{
                    progressDialog.hide()
                    response.message?.let{
                        Toast.makeText(requireContext(),"Error Occured : $it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        when(viewModel.user){
            "admin" -> {
                val admin = viewModel.adminData
                if(admin != null){
                    binding.apply {
                        layoutDepartment.visibility = View.GONE
                        layoutDivision.visibility = View.GONE
                        layoutDob.visibility = View.GONE
                        layoutMobile.visibility = View.GONE
                        layoutYear.visibility = View.GONE
                        layoutRegistrationNo.visibility = View.GONE
                        layoutPassingYear.visibility = View.GONE
                    }
                    binding.apply {
                        tvStudentEmailValue.text = admin.email
                        tvStudentNameValue.text = admin.name
                        tvStudentUserNameValue.text = admin.username
                        Glide.with(this@ProfileFragment).load(admin.profile_image_url).signature(
                            ObjectKey(viewModel.modified)
                        ).into(binding.userProfilePhoto);
                    }
                }
                viewModel.signOutResultAdmin.observe(requireActivity(),androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            response.data?.let {
                                viewModel.onAuthSignOutSaveToDatastore()
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
            "student" -> {
                val student = viewModel.studentData
                if(student != null){
                    binding.apply {
                        tvStudentDepartmentValue.text = student.department
                        tvStudentDivisionValue.text = student.division
                        tvStudentDobValue.text = student.dob
                        tvStudentEmailValue.text = student.email
                        tvStudentMobileValue.text = student.mobile
                        tvStudentNameValue.text = student.name
                        tvStudentYearValue.text = student.year
                        tvStudentRegistrationNoValue.text = student.registration_no
                        tvStudentUserNameValue.text = student.username
                        tvStudentPassingYearValue.text = student.passing_year
                        Glide.with(this@ProfileFragment).load(student.profile_image_url).signature(ObjectKey(viewModel.modified)).into(binding.userProfilePhoto);

                    }
                }

                viewModel.signOutResultStudent.observe(requireActivity(),androidx.lifecycle.Observer { response ->
                    when(response){
                        is Resource.Success->{
                            progressDialog.hide()
                            response.data?.let {
                                viewModel.onAuthSignOutSaveToDatastore()
                                startActivity(Intent(requireContext(), SignInActivity::class.java))
                                requireActivity().finish()
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
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when(viewModel.user){
            "admin" -> {
                when(viewModel.adminData?.username){
                    "vinodpatildev" -> {
                        inflater.inflate(R.menu.menu_fragment_profile_super_admin, menu)
                    }
                    else -> {
                        inflater.inflate(R.menu.menu_fragment_profile_admin, menu)
                    }
                }
            }
            "student" -> {
                inflater.inflate(R.menu.menu_fragment_profile, menu)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun choosePhotoFromGallery() {
        if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val pickIntent = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
            gallaryLauncher.launch(pickIntent)
        }else{
            Dexter.withContext(requireActivity().applicationContext)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if(report!!.areAllPermissionsGranted()){
                            Toast.makeText(requireActivity().applicationContext,"Storage READ/WRITE permissions are granted.",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        val message = "It looks like you have turned off permission required for this feature. It can be enabled under the Application Settings."
                        showRationalDialogForPermissions(message)
                    }
                })
                .onSameThread()
                .check()
        }
    }
    private fun showRationalDialogForPermissions(message: String) {
        val dialog = AlertDialog.Builder(requireActivity().applicationContext)
            .setMessage(message)
            .setPositiveButton("GO TO SETTINGS"){_,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemProfileUpdateData -> {
                startActivity(Intent(requireActivity(),ProfileUpdateDataActivity::class.java))
                return true
            }
            R.id.menuItemProfileUpdatePassword-> {
                startActivity(Intent(requireActivity(),ProfileUpdatePasswordActivity::class.java))
                return true
            }
            R.id.menuItemProfileSignOut -> {
                viewModel.signOutStudent()
                return true
            }
            R.id.menuItemProfileCreateAdmin-> {
                startActivity(Intent(requireActivity(),RegisterAdminBySuperAdminActivity::class.java))
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
