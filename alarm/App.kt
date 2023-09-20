package com.example.myapplication

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * @author m.a.sachuk
 */
internal class App : Application() {

    private var lifecycleObserver: ApplicationLifecycleObserver? = null

    override fun onCreate() {
        super.onCreate()
        val observer = createLifecycleObserver()
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
    }

    private fun createLifecycleObserver(): ApplicationLifecycleObserver {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return ApplicationLifecycleObserver(
            alarmManager,
            createAlarmTriggeredIntent()
        ).also { lifecycleObserver = it }
    }

    private fun createAlarmTriggeredIntent(): PendingIntent {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
