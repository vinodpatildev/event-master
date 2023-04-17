package com.vinodpatildev.eventmaster.presentation.ui.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.skyfishjy.library.RippleBackground
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.data.model.Event
import com.vinodpatildev.eventmaster.data.util.Constants
import com.vinodpatildev.eventmaster.databinding.ActivityGeofenceBinding
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModel
import com.vinodpatildev.eventmaster.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.util.function.BinaryOperator
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ViewModel
    private lateinit var binding :  ActivityGeofenceBinding
    private lateinit var clickedEvent : Event
    private var geofencingClient : GeofencingClient? = null
    private val geofenceList: ArrayList<Geofence> = ArrayList()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadCastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                TimerService.TIMER_BROADCAST -> {
                    updateTimer(intent.getLongExtra("countdown_time", 0))
                }
                GeofenceBroadCastReceiver.GEOFENCE_TRIGGER_ENTER -> {
                    binding.rippleBackgroundGeofenceActive.visibility = View.GONE
                    binding.rippleBackgroundGeofenceActive.stopRippleAnimation()

                    binding.rippleBackgroundGeofenceMonitoringActive.visibility = View.VISIBLE
                    binding.rippleBackgroundGeofenceMonitoringActive.startRippleAnimation()
                }
                GeofenceBroadCastReceiver.GEOFENCE_TRIGGER_EXIT -> {
                    binding.rippleBackgroundGeofenceMonitoringActive.visibility = View.GONE
                    binding.rippleBackgroundGeofenceMonitoringActive.stopRippleAnimation()

                    binding.rippleBackgroundGeofenceActive.visibility = View.VISIBLE
                    binding.rippleBackgroundGeofenceActive.startRippleAnimation()
                }
                TimerService.SUCCESS_TIME_ATTENDED -> {
                    binding.rippleBackgroundGeofenceMonitoringActive.visibility = View.GONE
                    binding.rippleBackgroundGeofenceMonitoringActive.stopRippleAnimation()

                    binding.rippleBackgroundGeofenceActive.visibility = View.GONE
                    binding.rippleBackgroundGeofenceActive.stopRippleAnimation()
                    finish()
                    Snackbar.make(binding.root,"Successfully attended the event : ${clickedEvent.title}",Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)
        supportActionBar?.hide()
        clickedEvent = intent.getSerializableExtra(Event.TAG) as Event

        geofencingClient = LocationServices.getGeofencingClient(this)

        geofenceList.add(Geofence.Builder()
            .setRequestId(Constants.request_key)
            .setCircularRegion(
                clickedEvent.latitude?.toDouble() ?: Constants.latitude,
                clickedEvent.longitude?.toDouble() ?: Constants.longitude,
                Constants.GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build())

        binding.btnEnableDisableGeofence.setOnClickListener {
            if(!viewModel.geofenceEventMonitoring){
                addGeofence()
            }else{
                removeGeofence()
            }
        }
    }

    private fun addGeofence() {
        if (!isLocationEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            //Do the work
                            getGeofencingRequest()?.let { geofencingRequest ->
                                geofencePendingIntent?.let { geofencingPendingIntent ->
                                    geofencingClient!!.addGeofences(
                                        geofencingRequest,
                                        geofencingPendingIntent
                                    )
                                        .addOnSuccessListener(this@GeofenceActivity) {
                                            Snackbar.make(
                                                binding.root,
                                                "Geofencing has started",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                            viewModel.geofenceEventMonitoring = true
                                            binding.rippleBackgroundGeofenceActive.visibility = View.VISIBLE
                                            binding.rippleBackgroundGeofenceActive.startRippleAnimation()
                                        }
                                        .addOnFailureListener(this@GeofenceActivity) {
                                            Snackbar.make(
                                                binding.root,
                                                "Geofencing failed",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: MutableList<PermissionRequest>?,
                        report: PermissionToken?
                    ) {
                        val message =
                            "You need to enable location services in order to access map. You can do this in settings."
                        showRationalDialogForPermissions(message)
                    }

                })
                .onSameThread()
                .check()
        }
    }

    private fun removeGeofence() {
        if (!isLocationEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            //Do the work
                            geofencePendingIntent?.let { geofencingPendingIntent ->
                                geofencingClient?.removeGeofences(geofencingPendingIntent)
                                    ?.addOnSuccessListener(this@GeofenceActivity, OnSuccessListener {
                                        Snackbar.make(binding.root,"Geofencing has been removed",Snackbar.LENGTH_LONG).show()
                                        viewModel.geofenceEventMonitoring = false
                                        binding.rippleBackgroundGeofenceActive.visibility = View.GONE
                                        binding.rippleBackgroundGeofenceActive.stopRippleAnimation()
                                    })
                                    ?.addOnFailureListener(this@GeofenceActivity, OnFailureListener {
                                        Snackbar.make(binding.root,"Geofencing could not be removed",Snackbar.LENGTH_LONG).show()
                                    })
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: MutableList<PermissionRequest>?,
                        report: PermissionToken?
                    ) {
                        val message =
                            "You need to enable location services in order to access map. You can do this in settings."
                        showRationalDialogForPermissions(message)
                    }

                })
                .onSameThread()
                .check()
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest? {
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(geofenceList)
        return builder.build()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter().apply {
            addAction(GeofenceBroadCastReceiver.GEOFENCE_TRIGGER_ENTER)
            addAction(GeofenceBroadCastReceiver.GEOFENCE_TRIGGER_EXIT)
            addAction(TimerService.TIMER_BROADCAST)
            addAction(TimerService.SUCCESS_TIME_ATTENDED)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun updateTimer(elapsedTime: Long) {
        val minutes = (elapsedTime / 1000) / 60
        val seconds = (elapsedTime / 1000) % 60
        binding.tvStatus.setText(String.format("%02d:%02d", minutes, seconds))
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
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