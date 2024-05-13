package com.example.alarmclock.models

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarmclock.NotificationActivity
import com.example.alarmclock.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        //Intent
        val nextActivity = Intent(context, NotificationActivity::class.java)
        nextActivity.flags.apply {
            Intent.FLAG_ACTIVITY_NEW_TASK
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        //pendingIntent(Intent)
        val pendingIntent = PendingIntent.getActivity(context, 0, nextActivity, 0)

        //Notification Build
        val builder = NotificationCompat.Builder(context!!, "android_knowledge")
        builder.apply {
            setSmallIcon(R.drawable.baseline_notifications_24)
            setContentTitle("Reminder")
            setContentText("it's Time to Wake Up")
            setAutoCancel(true)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setContentIntent(pendingIntent)
        }
        //Notification Manager
        //notify builder
        val notificationManager = NotificationManagerCompat.from(context)
        if (if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }) { return }
        notificationManager.notify(123, builder.build())

    }
}