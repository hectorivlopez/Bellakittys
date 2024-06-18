package com.movil.bellakkitys

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MyApp : Application(){

    companion object{
        const val CHANNEL_ID = "my_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Concert Updates",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "This channel shows concerts updates"
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

}