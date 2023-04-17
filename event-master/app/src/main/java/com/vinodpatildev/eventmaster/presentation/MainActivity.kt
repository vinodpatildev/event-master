package com.vinodpatildev.eventmaster.presentation

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityMainBinding
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.ui.event.editor.EventCreateEditActivity
import com.vinodpatildev.eventmaster.presentation.ui.home.HomeFragment
import com.vinodpatildev.eventmaster.presentation.ui.notification.NotificationFragment
import com.vinodpatildev.eventmaster.presentation.ui.profile.ProfileFragment
import com.vinodpatildev.eventmaster.presentation.ui.registered_created.RegisteredCreatedFragment
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel

    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding

//    lateinit var userType:String
//    var student: Student? = null
//    var admin: Admin? = null
//    var userStatus: Boolean = false

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.MainContainer, HomeFragment.newInstance())
                .commitNow()
        }
        toggle = ActionBarDrawerToggle(this,binding.drawerLayoutHome,R.string.open, R.string.close)
        binding.drawerLayoutHome.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing out....")
        progressDialog.setCancelable(false)

//        fillDefaultData()
        setDrawerLayoutNavigationListeners()
        setDrawerLayoutNavigationHeaderLayout()

        when(viewModel.user){
            "admin" -> {
                viewModel.signOutResultAdmin.observe(this, Observer { response->
                    when(response){
                        is Resource.Success -> {
                            progressDialog.hide()
                            viewModel.onAuthSignOutSaveToDatastore()
                            startActivity(Intent(this,SignInActivity::class.java))
                            finish()
                        }
                        is Resource.Loading -> {
                            progressDialog.show()
                        }
                        is Resource.Error -> {
                            progressDialog.hide()
                            response.message?.let{
                                Snackbar.make(binding.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                })
            }
            "student" -> {
                viewModel.signOutResultStudent.observe(this, Observer { response->
                    when(response){
                        is Resource.Success -> {
                            progressDialog.hide()
                            viewModel.onAuthSignOutSaveToDatastore()
                            startActivity(Intent(this,SignInActivity::class.java))
                            finish()
                        }
                        is Resource.Loading -> {
                            progressDialog.show()
                        }
                        is Resource.Error -> {
                            progressDialog.hide()
                            response.message?.let{
                                Snackbar.make(binding.root,"Error Occured:$it", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                })
            }
        }
    }
//    private fun fillDefaultData(){
//        CoroutineScope(Dispatchers.Default).launch {
//            viewModel.userStatus.collect{
//                withContext(Dispatchers.Main){
//                    userStatus = it
//                }
//            }
//        }
//        CoroutineScope(Dispatchers.Default).launch {
//            viewModel.userType.collect{
//                withContext(Dispatchers.Main){
//                    userType = it;
//                }
//            }
//        }
//        when(viewModel.user){
//            "student" -> {
//                CoroutineScope(Dispatchers.Default).launch {
//                    viewModel.userDataStudent.collect{
//                        withContext(Dispatchers.Main){
//                            student = it.data;
//                            setDrawerLayoutNavigationHeaderLayout()
//                        }
//                    }
//                }
//            }
//            "admin" -> {
//                CoroutineScope(Dispatchers.Default).launch {
//                    viewModel.userDataAdmin.collect{
//                        withContext(Dispatchers.Main){
//                            admin = it.data;
//                            setDrawerLayoutNavigationHeaderLayout()
//                        }
//                    }
//                }
//            }
//
//
//        }
//    }

    private fun setDrawerLayoutNavigationHeaderLayout() {
        viewModel.modifiedLiveData.observe(this, Observer {
            when(viewModel.user){
                "admin" -> {
                    Glide.with(this).load(viewModel.adminData?.profile_image_url).signature(ObjectKey(viewModel.modified)).into(binding.drawerNavigationViewHome.getHeaderView(0).findViewById<ImageView>(R.id.ivNavHeaderPersonProfilePicture));
                }
                "student" -> {
                    Glide.with(this).load(viewModel.studentData?.profile_image_url).signature(ObjectKey(viewModel.modified)).into(binding.drawerNavigationViewHome.getHeaderView(0).findViewById<ImageView>(R.id.ivNavHeaderPersonProfilePicture));
                }
            }
        })
        when(viewModel.user){
            "student" -> {
                viewModel.studentData?.let { user->
                    Glide.with(this).load(user.profile_image_url).signature(ObjectKey(viewModel.modified)).into(binding.drawerNavigationViewHome.getHeaderView(0).findViewById<ImageView>(R.id.ivNavHeaderPersonProfilePicture));
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonName).text = user.name
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonRegistrationNo).text = user.registration_no
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonMail).text = user.email
                }
            }
            "admin" -> {
                viewModel.adminData?.let { user->
                    Glide.with(this).load(user.profile_image_url).signature(ObjectKey(viewModel.modified)).into(binding.drawerNavigationViewHome.getHeaderView(0).findViewById<ImageView>(R.id.ivNavHeaderPersonProfilePicture));
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonName).text = user.username
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonRegistrationNo).text = user.name
                    binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonMail).text = user.email
                }
            }
        }
    }

    private fun setDrawerLayoutNavigationListeners() {
        if(viewModel.user == "admin"){
            val registeredEventsMenuItem = binding.drawerNavigationViewHome.menu.findItem(R.id.menuItemDrawerRegisteredEvents)
            registeredEventsMenuItem.setTitle("Created Events")

            var menuSize = binding.drawerNavigationViewHome.menu.size()
            binding.drawerNavigationViewHome.menu.apply {
                add(1,menuSize,menuSize,"Create Event")
                getItem(menuSize).setOnMenuItemClickListener {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this@MainActivity, EventCreateEditActivity::class.java))
                    false
                }
            }

        }
        binding.drawerNavigationViewHome.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menuItemDrawerHome -> {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer,HomeFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerRegisteredEvents -> {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer, RegisteredCreatedFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerProfile -> {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer, ProfileFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerNotification -> {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer,NotificationFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerSignOut -> {
                    when(viewModel.user){
                        "student" -> viewModel.signOutStudent()
                        "admin" -> viewModel.signOutAdmin()
                    }
                }
            }
            true;
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}