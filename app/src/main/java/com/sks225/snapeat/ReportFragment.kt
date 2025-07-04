package com.sks225.snapeat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sks225.snapeat.databinding.FragmentReportBinding
import com.sks225.snapeat.utilities.bmiCategory
import com.sks225.snapeat.utilities.setUpGauge
import com.sks225.snapeat.viewModel.MainFragmentViewModel
import java.util.Calendar
import java.util.Locale

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]

        setupUI()
        observeBmi()
        observeLastWeekCalories()

        return binding.root
    }

    private fun setupUI() {
        binding.cvBmi.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calculateBmiFragment)
        }
    }

    private fun observeBmi() {
        viewModel.bmiRecord.observe(viewLifecycleOwner) { bmiRecord ->
            Log.d("ReportFragment", "BMI Record: $bmiRecord ${bmiRecord?.bmi}")
            if (bmiRecord != null) {
                val bmiValue = String.format("%.2f", bmiRecord.bmi)
                binding.tvBmi.text = "Your BMI is $bmiValue"
                binding.tvBmiCategory.text = "You are ${bmiCategory(bmiRecord.bmi)}"
                setUpGauge(binding.gaugeCard1, bmiRecord.bmi)
            } else {
                binding.tvBmi.text = "BMI not found"
                binding.tvBmiCategory.text = "Calculate your BMI here!!"
                setUpGauge(binding.gaugeCard1, 0f)
            }
        }
    }

    private fun observeLastWeekCalories() {
        viewModel.lastWeekCalories.observe(viewLifecycleOwner) { calories ->
            val totalCalories = calories.sum().toInt()
            val avgCalories = calories.average().toInt()

            binding.tvTotalCalories.text = "$totalCalories Cal"
            binding.tvAvgCalories.text = "$avgCalories Cal"

            val labels = getLastWeekLabels()
            val entries = calories.mapIndexed { index, calorie ->
                BarEntry(index.toFloat(), calorie.toFloat())
            }

            val dataSet = BarDataSet(entries, "Calories").apply {
                valueTextColor = Color.BLACK
                valueTextSize = 12f
            }

            val barData = BarData(dataSet)
            binding.barChart.data = barData

            binding.barChart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                granularity = 1f
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.BLACK
                textSize = 12f
                labelRotationAngle = -45f
            }

            binding.barChart.axisLeft.apply {
                axisMinimum = 0f
                textColor = Color.BLACK
            }
            binding.barChart.axisRight.isEnabled = false

            binding.barChart.apply {
                description.isEnabled = false
                legend.isEnabled = false
                setFitBars(true)
                invalidate()
            }

            binding.barChart.setExtraOffsets(0f, 0f, 0f, 64f)
        }
    }

    fun getLastWeekLabels(): List<String> {
        val labels = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        for (offset in 0 until 7) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_YEAR, -offset)

            val label = when (offset) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> calendar.getDisplayName(
                    Calendar.DAY_OF_WEEK,
                    Calendar.LONG,
                    Locale.getDefault()
                ) ?: "Unknown"
            }

            labels.add(label)
        }

        return labels
    }
}
