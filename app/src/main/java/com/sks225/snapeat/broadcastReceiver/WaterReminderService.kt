package com.sks225.snapeat.broadcastReceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.sks225.snapeat.MainActivity
import com.sks225.snapeat.R

class WaterReminderService : Service() {
    private lateinit var ringtone: Ringtone
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.action == "STOP_ALARM") {
            ringtone.stop()
            stopSelf()
        } else {
            onHandleIntent()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onHandleIntent() {

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        resultIntent.action = "STOP_SERVICE"

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            1,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                "ID2",
                "Water reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Water Reminder Notification Channel"
                enableVibration(true)
                setSound(null, null)
            }
            mNotificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "ID2")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Water Reminder")
                .setContentText("It's time to have a glass of water ;)")
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
        ringtone.play()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_ID = 5
    }
}