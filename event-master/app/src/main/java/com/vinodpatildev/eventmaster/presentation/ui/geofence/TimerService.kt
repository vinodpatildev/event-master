package com.vinodpatildev.eventmaster.presentation.ui.geofence

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
    private var timeRemaining = 60000L
    private val broadcastIntent = Intent(TIMER_BROADCAST)
    private val successTimeAttendedIntent = Intent(SUCCESS_TIME_ATTENDED)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            var intentNotificationMessage = ""
            when (it.action) {
                "TIMER_ACTION" -> {
                    when (intent?.getStringExtra("action_type")) {
                        RESUME -> {
                            intentNotificationMessage = "Timer resumed."
                            resumeTimer()
                        }
                        PAUSE -> {
                            intentNotificationMessage = "Timer paused."
                            pauseTimer()
                        }
                        else -> {
                            intentNotificationMessage = "Unknown timer action."
                        }
                    }
                }
                else -> {
                    intentNotificationMessage = "Not timer action"
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val geofenceActivityIntent = Intent(this,GeofenceActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    geofenceActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
                val notification: Notification = Notification.Builder(this, EventMasterApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_dialog_email)
                    .setContentTitle("Timer Service")
                    .setContentText(intentNotificationMessage)
                    .setContentIntent(pendingIntent)
                    .build()
                startForeground(1,notification)
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
