package com.vinodpatildev.eventmaster.presentation.ui.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vinodpatildev.eventmaster.R
import com.vinodpatildev.eventmaster.presentation.EventMasterApp
import com.vinodpatildev.eventmaster.presentation.MainActivity

class NotificationFirebaseMessagingService : FirebaseMessagingService() {
    private var notificationManager: NotificationManagerCompat? = null
    companion object{
        val CHANNEL_4_ID = "CHANNEL4"
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel4 = NotificationChannel(
                NotificationFirebaseMessagingService.CHANNEL_4_ID,
                "Channel 4",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel4.description = "This is channel 4"
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel4)
        }
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the message
        Log.d("event-master-fcm", "Message data payload: " + remoteMessage.data)

        val notification = remoteMessage.notification

            val title = remoteMessage.data.get("title")
            val body = remoteMessage.data.get("body")
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val largeImage = BitmapFactory.decodeResource(resources, R.drawable.kakashi)

            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, CHANNEL_4_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setLargeIcon(largeImage)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setColor(Color.BLUE)
                    .setColorized(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(body)
                            .setBigContentTitle(title)
                            .setSummaryText("Join this event.")
                    )
            notificationManager?.notify(66, notificationBuilder.build())

            super.onMessageReceived(remoteMessage)
    }
}


