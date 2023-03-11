package com.vinodpatildev.eventmaster.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.databinding.ActivityMainBinding
import com.vinodpatildev.eventmaster.presentation.ui.event.editor.EventEditActivity
import com.vinodpatildev.eventmaster.presentation.ui.home.HomeFragment
import com.vinodpatildev.eventmaster.presentation.ui.login.LoginActivity
import com.vinodpatildev.eventmaster.presentation.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var userType:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.MainContainer, HomeFragment.newInstance())
                .commitNow()
        }
        auth = FirebaseAuth.getInstance()
        userType = "student"
        userType = intent.getStringExtra("signInUserType").toString();
        toggle = ActionBarDrawerToggle(this,binding.drawerLayoutHome,R.string.open, R.string.close)
        binding.drawerLayoutHome.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDrawerLayoutNavigationListeners()
        setDrawerLayoutNavigationHeaderLayout()
    }

    private fun setDrawerLayoutNavigationHeaderLayout() {
        val user = auth.currentUser
        user?.let {
            val uid = user.uid
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val emailVerified = user.isEmailVerified

            Glide.with(this).load(photoUrl).into(binding.drawerNavigationViewHome.getHeaderView(0).findViewById<ImageView>(R.id.ivNavHeaderPersonProfilePicture));
            binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonName).text = name.toString()
            binding.drawerNavigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.tvNavHeaderPersonMail).text = email.toString()
         }
    }

    private fun setDrawerLayoutNavigationListeners() {
        if(userType == "admin"){
            var menuSize = binding.drawerNavigationViewHome.menu.size
            binding.drawerNavigationViewHome.menu.apply {
                add(1,menuSize,menuSize,"Create Event")
                getItem(menuSize).setOnMenuItemClickListener {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this@MainActivity,EventEditActivity::class.java))
                    false
                }
            }
        }
        binding.drawerNavigationViewHome.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menuItemDrawerHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer,HomeFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerMessages -> {
                    Toast.makeText(this,"Messages selected.", Toast.LENGTH_LONG).show()
                }
                R.id.menuItemDrawerProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.MainContainer, ProfileFragment.newInstance())
                        .commitNow()
                }
                R.id.menuItemDrawerSignOut -> {
                    binding.drawerLayoutHome.closeDrawer(GravityCompat.START)
                    auth.signOut()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
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
}