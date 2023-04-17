package com.vinodpatildev.eventmaster.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventMasterApp: Application() {
    companion object{
        val CHANNEL_1_ID = "CHANNEL1"
        val CHANNEL_2_ID = "CHANNEL2"
        val CHANNEL_3_ID = "CHANNEL3"
        val NOTIFICATION_GROUP_1_ID = "NOTIFICATION_GROUP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            val channel2 = NotificationChannel(
                CHANNEL_2_ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_HIGH
            )
            val channel3 = NotificationChannel(
                CHANNEL_3_ID,
                "Channel 3",
                NotificationManager.IMPORTANCE_MIN
            )

            val group_important = NotificationChannelGroup(
                NOTIFICATION_GROUP_1_ID,
                "IMPORTANT"
            )
            channel1.description = "This is channel 1"
            channel2.description = "This is channel 2"
            channel3.description = "This is channel 3"
            channel1.group = NOTIFICATION_GROUP_1_ID
            channel2.group = NOTIFICATION_GROUP_1_ID
            channel3.group = NOTIFICATION_GROUP_1_ID
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannelGroup(group_important)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
            manager.createNotificationChannel(channel3)
        }
    }

}