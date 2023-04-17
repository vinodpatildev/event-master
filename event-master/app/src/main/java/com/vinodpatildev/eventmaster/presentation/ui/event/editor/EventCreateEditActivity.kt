package com.vinodpatildev.eventmaster.presentation.ui.event.editor

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.util.Constants
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.databinding.ActivityEventCreateEditBinding
import com.vinodpatildev.eventmaster.presentation.MainActivity
import com.vinodpatildev.eventmaster.presentation.ui.auth.SignInActivity
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.vinodpatildev.happyplaces.utils.GetAddressFromLatLng
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


@AndroidEntryPoint
class EventCreateEditActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityEventCreateEditBinding
    private lateinit var progressDialog: ProgressDialog

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    //Location
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mPlaceDetails : String? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private val mapLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ){
            result ->
        if(result.resultCode == RESULT_OK){
            Log.d("MAP","map launched")
            val place: Place? = result.data?.let { Autocomplete.getPlaceFromIntent(it) }
            binding?.etLocation?.setText(place?.address)
            mLatitude = place?.latLng!!.latitude
            mLongitude = place?.latLng!!.longitude
        }
    }
    //Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventCreateEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory ).get(ViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Creating Event...")
        progressDialog.setCancelable(false)

        val typeOptions = resources.getStringArray(R.array.type_options)
        val typeArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, typeOptions)
        binding.etType.setAdapter(typeArrayAdapter)

        //Location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if(!Places.isInitialized()){
            Places.initialize(this, resources.getString((R.string.google_maps_key)))
        }
        binding.etLocation.setOnClickListener {
            try {
                val fields = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
                mapLauncher.launch(intent)

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding.btnSelectCurrentLocation.setOnClickListener {
            if(!isLocationEnabled()){
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }else{
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(object: MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if(report!!.areAllPermissionsGranted()){
                                requestNewLocationData()
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            permission: MutableList<PermissionRequest>?,
                            report: PermissionToken?
                        ) {
                            val message = "You need to enable location services in order to access map. You can do this in settings."
                            showRationalDialogForPermissions(message)
                        }

                    })
                    .onSameThread()
                    .check()
            }
        }

        //Location

        //Date
        binding.etStartDatetime.apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
                val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                // Create date picker dialog
                val datePickerDialog = DatePickerDialog(this@EventCreateEditActivity,
                    { _, year, month, day ->
                        // Set selected date to calendar
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, day)

                        // Create time picker dialog
                        val timePickerDialog = TimePickerDialog(this@EventCreateEditActivity,
                            { _, hour, minute ->
                                // Set selected time to calendar
                                cal.set(Calendar.HOUR_OF_DAY, hour)
                                cal.set(Calendar.MINUTE, minute)

                                // Format date and time as per requirement
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                                val formattedDateTime = dateFormat.format(cal.time)

                                // Set formatted date and time to edit text
                                binding.etStartDatetime.setText(formattedDateTime)
                            }, hourOfDay, minute, true)
                        timePickerDialog.show()
                    }, year, month, dayOfMonth)
                datePickerDialog.show()
            }
        }
        binding.etEndDatetime.apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
                val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                // Create date picker dialog
                val datePickerDialog = DatePickerDialog(this@EventCreateEditActivity,
                    { _, year, month, day ->
                        // Set selected date to calendar
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, day)

                        // Create time picker dialog
                        val timePickerDialog = TimePickerDialog(this@EventCreateEditActivity,
                            { _, hour, minute ->
                                // Set selected time to calendar
                                cal.set(Calendar.HOUR_OF_DAY, hour)
                                cal.set(Calendar.MINUTE, minute)

                                // Format date and time as per requirement
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                                val formattedDateTime = dateFormat.format(cal.time)

                                // Set formatted date and time to edit text
                                binding.etEndDatetime.setText(formattedDateTime)
                            }, hourOfDay, minute, true)
                        timePickerDialog.show()
                    }, year, month, dayOfMonth)
                datePickerDialog.show()
            }
        }

        //Department options
        val departmentOptions = resources.getStringArray(R.array.department_options)
        val departmentArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, departmentOptions)
        binding.etDepartment.setAdapter(departmentArrayAdapter)



        viewModel.createEventAdminResult.observe(this, Observer { response->
            when(response){
                is Resource.Success -> {
                    progressDialog.hide()
                    startActivity(Intent(this, MainActivity::class.java))
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

        binding.btnCreateEdit.setOnClickListener {
            if(binding.etName.text?.trim().toString().isEmpty()){
                binding.etName.error = "Please enter name."
                binding.etName.requestFocus()
            }else if(binding.etType.text?.trim().toString().isEmpty()){
                binding.etType.error = "Please enter the type of event."
                binding.etType.requestFocus()
            }else if(binding.etDescription.text?.trim().toString().isEmpty()){
                binding.etDescription.error = "Please enter description."
                binding.etDescription.requestFocus()
            }else if(binding.etLocation.text?.trim().toString().isEmpty()){
                binding.etLocation.error = "Please select the location for evenue."
                binding.etLocation.requestFocus()
            }else if(binding.etStartDatetime.text?.trim().toString().isEmpty()){
                binding.etStartDatetime.error = "Select start time."
                binding.etStartDatetime.requestFocus()
            }else if(binding.etEndDatetime.text?.trim().toString().isEmpty()){
                binding.etEndDatetime.error = "Select end time."
                binding.etEndDatetime.requestFocus()
            }else if(binding.etDepartment.text?.trim().toString().isEmpty()){
                binding.etDepartment.error = "Select department."
                binding.etDepartment.requestFocus()
            }else if(binding.etOrganizer.text?.trim().toString().isEmpty()){
                binding.etOrganizer.error = "Enter organizer name."
                binding.etOrganizer.requestFocus()
            }else{
                val name = binding.etName.text?.trim().toString()
                val type = binding.etType.text?.trim().toString()
                val description = binding.etDescription.text?.trim().toString()
                val location = binding.etLocation.text?.trim().toString()
                val eventLink = binding.etEventLink.text?.trim().toString()
                val startTime = binding.etStartDatetime.text?.trim().toString()
                val endTime = binding.etEndDatetime.text?.trim().toString()
                val department = binding.etDepartment.text?.trim().toString()
                val organizer = binding.etOrganizer.text?.trim().toString()
                val longitude = mLongitude.toString()
                val latitude = mLatitude.toString()
                viewModel.createEventAdmin( name, type, description, location, eventLink, startTime, endTime, department, organizer, longitude, latitude)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.setTitle("Create Event")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
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

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())

    }

    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            val mLastLocation: Location = result.lastLocation!!
            mLatitude = mLastLocation.latitude
            mLongitude = mLastLocation.longitude
            Log.d("TESLA-LOG","lattitude: $mLatitude")
            Log.d("TESLA-LOG","longitude: $mLongitude")

            val addressTask = GetAddressFromLatLng(this@EventCreateEditActivity, mLatitude, mLongitude)
            addressTask.setAddressListener(object: GetAddressFromLatLng.AddressListener{
                override fun onAddressFound(address:String?){
                    binding?.etLocation?.setText(address)
                    Log.e("TESTA-LOG","Address found $address")
                }
                override fun onError(){
                    Log.e("TESTA-LOG","Get Address - Something went wrong.")
                }
            })
            addressTask.getAddress()
        }
    }
    private fun showRationalDialogForPermissions(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("GO TO SETTINGS"){_,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", packageName, null)
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
}