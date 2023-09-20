package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.TimeUnit

internal class ApplicationLifecycleObserver(
    private val alarmManager: AlarmManager,
    private val triggerIntent: PendingIntent,
) : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        println("APPLICATION ONPAUSE")
        val triggerTimeInMillis = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1L)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerTimeInMillis,
            triggerIntent
        )
    }
}
