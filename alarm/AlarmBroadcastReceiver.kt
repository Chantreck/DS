package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import kotlin.random.Random

internal class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"
        notificationManager.createChannel(channelId)
        val notification = createNotification(context, channelId)
        notificationManager.notify(Random.nextInt(0, 1000), notification)
    }

    private fun NotificationManager.createChannel(id: String) {
        val name = "default_channel"
        val channel =
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        createNotificationChannel(channel)
    }

    private fun createNotification(context: Context, channelId: String): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Notification Title")
            .setContentText("Notification by schedule message")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }
}
