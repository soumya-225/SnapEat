package com.sks225.snapeat

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sks225.snapeat.broadcastReceiver.AlarmService
import com.sks225.snapeat.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var medicinesList: ArrayList<String>
    private var allChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, AlarmService::class.java)
        serviceIntent.action = "STOP_ALARM"
        startService(serviceIntent)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.notification)
        mediaPlayer.start()

        if (intent.extras != null) {
            binding.tvTitle.text = intent.getStringExtra("TITLE")
            binding.ivIcon.setImageResource(intent.getIntExtra("ICON", 0))
            binding.tvTime.text = intent.getStringExtra("TIME")
            medicinesList = intent.getStringArrayListExtra("MEDICINES")!!
        }

        Glide.with(applicationContext).load(R.drawable.alert).into(binding.imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}