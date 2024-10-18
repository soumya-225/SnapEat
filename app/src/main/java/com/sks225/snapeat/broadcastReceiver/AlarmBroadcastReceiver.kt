package com.sks225.snapeat.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.sks225.snapeat.AlarmActivity

class AlarmBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val CUSTOM_ACTION = "com.example.discure.ACTION_ALARM"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == CUSTOM_ACTION) {
            val title = intent.getStringExtra("TITLE")
            val icon = intent.getStringExtra("ICON")
            val medicines = intent.getStringArrayListExtra("MEDICINES")
            val time = intent.getStringExtra("TIME")

            val serviceIntent = Intent(context, AlarmService::class.java).apply {
                putExtra("TITLE", title)
                putExtra("ICON", icon)
                putStringArrayListExtra("MEDICINES", medicines)
                putExtra("TIME", time)
            }
            context.startService(serviceIntent)

            val i = Intent(context, AlarmActivity::class.java).apply {
                putExtra("TITLE", title)
                putExtra("ICON", icon)
                putStringArrayListExtra("MEDICINES", medicines)
                putExtra("TIME", time)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(i)
        }
    }
}
