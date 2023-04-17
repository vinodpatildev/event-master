package com.vinodpatildev.eventmaster.presentation.ui.geofence

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.vinodpatildev.eventmaster.presentation.EventMasterApp


class GeofenceBroadCastReceiver : BroadcastReceiver() {
    companion object {
        val GEOFENCE_TRIGGER_ENTER = "geofence_triggered_enter"
        val GEOFENCE_TRIGGER_EXIT = "geofence_triggered_exit"
    }
    private val TAG = "EventMasterGeofenceLog"
    private var serviceStarted = false
    private val geofenceEnteredIntent = Intent(GEOFENCE_TRIGGER_ENTER)
    private val geofenceExitedIntent = Intent(GEOFENCE_TRIGGER_EXIT)


    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent!!.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent!!.errorCode)
            sendNotification("error:$errorMessage", context);
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            if (!serviceStarted) {
                val serviceIntent = Intent(context, TimerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context?.startForegroundService(serviceIntent);
                }else{
                    context?.startService(serviceIntent);
                }
                serviceStarted = true
                Log.i(TAG,"Started Timer Service")
            }
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val locId: String? = triggeringGeofences?.get(0)?.requestId

            when(geofenceTransition){
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    sendNotification("You have entered in $locId",context)
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(geofenceEnteredIntent)
                    resumePauseTimerService(context!!,TimerService.RESUME)
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    sendNotification("You have exited the $locId",context)
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(geofenceExitedIntent)
                    resumePauseTimerService(context!!,TimerService.PAUSE)
                }
            }
        }else{
            Log.e(TAG, "Error")
        }
    }

    private fun sendNotification(message: String?, context: Context?){
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context!!, EventMasterApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_dialog_email)
                .setContentTitle("Geofence Alert!!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager?.notify(77, notificationBuilder.build())
    }

    private fun resumePauseTimerService(context:Context, timerServiceCommand : String){
        val intent = Intent(context, TimerService::class.java)
        intent.action = "TIMER_ACTION"
        intent.putExtra("action_type", timerServiceCommand)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
        Log.i(TAG,"GeofenceBroadCastReceiver: resumePauseTimerService($timerServiceCommand)")
    }
}
