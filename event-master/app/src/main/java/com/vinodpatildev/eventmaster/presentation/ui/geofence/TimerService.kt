package com.vinodpatildev.eventmaster.presentation.ui.geofence

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vinodpatildev.eventmaster.presentation.EventMasterApp


class TimerService : Service() {
    private val TAG = "EventMasterGeofenceLog"
    companion object {
        val TIMER_BROADCAST = "timer_broadcast_ramayana"
        val SUCCESS_TIME_ATTENDED = "SUCCESS_TIME_ATTENDED"
        val RESUME = "resume"
        val PAUSE = "pause"
    }
    private var isTimerRunning = false
    private lateinit var countDownTimer: CountDownTimer
    private var timeRemaining = 5*60000L
    private val broadcastIntent = Intent(TIMER_BROADCAST)
    private val successTimeAttendedIntent = Intent(SUCCESS_TIME_ATTENDED)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
//            val CHANNEL_ID = "my_channel_01"
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
//                channel
//            )


            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, EventMasterApp.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_dialog_email)
                    .setContentTitle("Timer Service Started")
                    .setContentText("Error maintainance notification")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager?.notify(77, notificationBuilder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "TIMER_ACTION" -> {
                    when (intent?.getStringExtra("action_type")) {
                        RESUME -> resumeTimer()
                        PAUSE -> pauseTimer()
                        else -> {}
                    }
                }
                else -> {}
            }
        }
        return START_REDELIVER_INTENT
    }

    private fun resumeTimer() {
        if (!isTimerRunning) {
            countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                    broadcastIntent.putExtra("countdown_time", millisUntilFinished)
                    LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(broadcastIntent)
                    Log.i(TAG,"Timer :sent broadcast countdown time : $timeRemaining")
                }
                override fun onFinish() {
                    LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(successTimeAttendedIntent)
                    stopSelf()
                }
            }.start()
            isTimerRunning = true
        }
    }

    private fun pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pauseTimer()
    }
}
