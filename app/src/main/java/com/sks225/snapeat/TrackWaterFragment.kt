package com.sks225.snapeat

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.broadcastReceiver.WaterReminderBroadcastReceiver
import com.sks225.snapeat.broadcastReceiver.WaterReminderService
import com.sks225.snapeat.databinding.FragmentTrackWaterBinding
import com.sks225.snapeat.utilities.setData
import com.sks225.snapeat.utilities.setupBarChart
import java.util.Calendar


class TrackWaterFragment : Fragment() {
    private lateinit var binding: FragmentTrackWaterBinding
    private var count: Int = 0
    private var maxCount: Int = 0
    private var startTime: String = "9:00 AM"
    private var endTime: String = "9:00 PM"
    private var hour: Int = 0
    private var minute: Int = 0
    private var amPm: String = ""
    private var time: String = ""
    private lateinit var alarmManager: AlarmManager
    private var isStartTimeClicked = false
    private var isEndTimeClicked = false
    //private lateinit var viewModel: WaterTrackViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackWaterBinding.inflate(inflater, container, false)

//        val healthRepository = HealthDataRepository()
//        val goalRepository = SetGoalRepository()
//        val factory = WaterTrackViewModelFactory(goalRepository, healthRepository)
//        viewModel = ViewModelProvider(this, factory)[WaterTrackViewModel::class.java]

        val list = listOf(2, 3, 4, 6, 7, 8, 8, 8, 9)
        setupBarChart(binding.barChart)
        setData(binding.barChart, list)

        if (requireActivity().intent.action == "STOP_SERVICE")
            requireContext().stopService(Intent(requireContext(), WaterReminderService::class.java))

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        maxCount = 6

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDec.setOnClickListener {
            if (count > 0)
                count--
            updateCount()
        }

        binding.btnInc.setOnClickListener {
            if (count < maxCount)
                count++
            updateCount()
        }

        binding.linearProgress.max = maxCount
        binding.circularProgressIndicator.max = maxCount

        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.clReminder.visibility = View.VISIBLE
            else
                binding.clReminder.visibility = View.GONE
        }

        binding.tvStartTime.setOnClickListener {
            isStartTimeClicked = true
            showTimePickerDialog()
            binding.tvStartTime.text = startTime
        }

        binding.tvEndTime.setOnClickListener {
            isEndTimeClicked = true
            showTimePickerDialog()
            binding.tvEndTime.text = endTime
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_time -> {
                    binding.tvTimes.setTextColor(Color.GRAY)
                    binding.tvTime.setTextColor(Color.BLACK)
                    binding.rbTimes.setTextColor(Color.GRAY)
                    binding.rbTime.setTextColor(Color.BLACK)
                    binding.tvTime.setOnClickListener {
                        val inputDialog = WaterReminderDialogFragment("Remind me every:", "Hours")
                        inputDialog.setOnInputSubmittedListener {
                            val enteredHours = it.toInt()
                            setReminderWithTime(enteredHours)
                            binding.tvTime.text =
                                "$enteredHours ${if (enteredHours == 1) "hour" else "hours"}"
                            //viewModel.saveGoalWeight(enteredWeight)
                        }
                        inputDialog.show(childFragmentManager, "InputDialog1")
                    }
                }

                R.id.rb_times -> {
                    binding.tvTime.setTextColor(Color.GRAY)
                    binding.tvTimes.setTextColor(Color.BLACK)
                    binding.rbTime.setTextColor(Color.GRAY)
                    binding.rbTimes.setTextColor(Color.BLACK)
                    binding.tvTimes.setOnClickListener {
                        val inputDialog = WaterReminderDialogFragment("Remind me:", "Times")
                        inputDialog.setOnInputSubmittedListener {
                            val enteredTimes = it.toInt()
                            binding.tvTimes.text = "$enteredTimes times"
                            setReminderWithTimes(enteredTimes)
                            //viewModel.saveGoalWeight(enteredWeight)
                        }
                        inputDialog.show(childFragmentManager, "InputDialog2")
                    }

                }

            }
        }
        return binding.root
    }

    private fun updateCount() {
        binding.tvGlassCount.text = "$count of $maxCount Glasses"
        binding.linearProgress.progress = count
        binding.circularProgressIndicator.progress = count

        //viewModel.saveHealthData()
    }

    private fun showTimePickerDialog() {
        //val calendar = Calendar.getInstance()
        //val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        //val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinute = 0
        var currentHour = 0
        if (isStartTimeClicked) {
            currentHour = startTime.split(":")[0].toInt()
        } else if (isEndTimeClicked) {
            currentHour = endTime.split(":")[0].toInt()
        }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                hour = hourOfDay
                this.minute = minute
                amPm = if (hourOfDay < 12) "AM" else "PM"
                time = String.format(
                    "%02d:%02d %s",
                    if (hourOfDay % 12 == 0) 12 else hourOfDay % 12,
                    minute,
                    amPm
                )
                Log.d("TAG", "$isStartTimeClicked $isEndTimeClicked")
                if (isStartTimeClicked) {
                    isStartTimeClicked = false
                    startTime = time
                    binding.tvStartTime.text = startTime
                } else if (isEndTimeClicked) {
                    isEndTimeClicked = false
                    endTime = time
                    binding.tvEndTime.text = endTime
                }
            }

        TimePickerDialog(
            requireContext(),
            timeSetListener,
            currentHour,
            currentMinute,
            false
        ).show()
    }

    private fun setReminderWithTime(hours: Int) {
        val startCalendar = Calendar.getInstance().apply {
            val startMinute = startTime.split(":")[1].split(" ")[0].toInt()
            set(Calendar.HOUR_OF_DAY, getHour(startTime))
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)
        }

        val endCalendar = Calendar.getInstance().apply {
            val endMinute = endTime.split(":")[1].split(" ")[0].toInt()
            set(Calendar.HOUR_OF_DAY, getHour(endTime))
            set(Calendar.MINUTE, endMinute)
            set(Calendar.SECOND, 0)
        }

        if (startCalendar.after(endCalendar)) {
            endCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(requireContext(), WaterReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var triggerTime = startCalendar.timeInMillis

        try {
            while (triggerTime <= endCalendar.timeInMillis) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                triggerTime += hours * 3600 * 1000
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(), "Exact alarm permission is not granted", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setReminderWithTimes(times: Int) {
        val hours = (getHour(endTime) - getHour(startTime)) / times
        setReminderWithTime(hours)
    }

    private fun getHour(time: String): Int {
        val hour = time.split(":")[0].toInt()

        return if (time.split(" ")[1] == "AM")
            hour
        else
            hour + 12
    }
}