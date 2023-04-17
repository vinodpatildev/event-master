package com.vinodpatildev.eventmaster.presentation.ui.geofence

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.NotificationCompat
import com.vinodpatildev.eventmaster.R

class LocationService : Service() {
    private val TAG = "EventMasterGeofenceLog"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.rolex) // replace with your own music file
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        Toast.makeText(this, "Music started", Toast.LENGTH_SHORT).show()

        // stop the music after 1 minute
        Thread {
            Thread.sleep(60000) // wait for 1 minute
            mediaPlayer.stop()
            mediaPlayer.release()
            stopSelf()
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}