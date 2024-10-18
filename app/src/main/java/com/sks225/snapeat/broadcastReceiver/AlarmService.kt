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
import com.sks225.snapeat.AlarmActivity
import com.sks225.snapeat.R

class AlarmService : Service() {
    private lateinit var ringtone: Ringtone
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.action == "STOP_ALARM") {
            ringtone.stop()
            stopSelf()
        } else {
            onHandleIntent(intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onHandleIntent(intent: Intent) {
        val title = intent.getStringExtra("TITLE")
        val icon = intent.getStringExtra("ICON")
        val medicines = intent.getStringArrayListExtra("MEDICINES")
        val time = intent.getStringExtra("TIME")

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val resultIntent = Intent(applicationContext, AlarmActivity::class.java).apply {
            action = AlarmBroadcastReceiver.CUSTOM_ACTION
            putExtra("TITLE", title)
            putExtra("ICON", icon)
            putStringArrayListExtra("MEDICINES", medicines)
            putExtra("TIME", time)
        }

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(applicationContext, AlarmService::class.java).apply {
            action = "STOP_ALARM"
        }

        val snoozePendingIntent = PendingIntent.getService(
            this,
            0,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                "ID",
                "Medicine Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Medicine Reminder Notification Channel"
                enableVibration(true)
                setSound(null, null)
            }
            mNotificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "ID")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Medicine Reminder")
                .setContentText("It's time to take your medicine")
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .addAction(R.drawable.baseline_add_24, "Take Medicines", resultPendingIntent)
                .addAction(R.drawable.baseline_notifications_24, "Snooze", snoozePendingIntent)
                .setContentIntent(resultPendingIntent)

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
        ringtone.play()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_ID = 3
    }
}