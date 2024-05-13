package com.example.alarmclock

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.alarmclock.databinding.ActivityMainBinding
import com.example.alarmclock.models.AlarmReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var timePacker : MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent : PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationCannel()

        setlistiener()

    }

    private fun setlistiener() {

        binding.txtSeletTime.setOnClickListener{ setTimePacker() }
        binding.btnSetAlarm.setOnClickListener{ setAlarm() }
        binding.btnCancelAlarm.setOnClickListener{ cancelAlarm() }
    }

    private fun cancelAlarm() {
        val intent = Intent(this , AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        if (alarmManager == null){
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this , AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show()
    }

    private fun setTimePacker(){
        timePacker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        timePacker.show(supportFragmentManager, "android_knowledge")

        timePacker.addOnPositiveButtonClickListener{
            if (timePacker.hour > 12){
                binding.txtSeletTime.text = String.format("%02d", (timePacker.hour - 12)) + ":" +
                        String.format("%02d", (timePacker.minute)) + " PM"
            }
            else{
                binding.txtSeletTime.text = "${timePacker.hour}:${timePacker.minute} AM"
            }
        }

        calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, timePacker.hour)
            set(Calendar.MINUTE, timePacker.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun createNotificationCannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "akchannel"
            val channel = NotificationChannel("android_knowledge", name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Channel for Alarm Manager"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}


