package com.sks225.snapeat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sks225.snapeat.databinding.FragmentTrackWaterBinding
import com.sks225.snapeat.model.WaterIntake
import com.sks225.snapeat.repository.WaterRepository
import com.sks225.snapeat.viewModel.WaterViewModel
import com.sks225.snapeat.viewModel.WaterViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import androidx.core.graphics.toColorInt

class TrackWaterFragment : Fragment() {
    private lateinit var binding: FragmentTrackWaterBinding
    private lateinit var viewModel: WaterViewModel
    private val maxCount = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackWaterBinding.inflate(inflater, container, false)

        // Create repo + factory manually
        val repo = WaterRepository()
        val factory = WaterViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[WaterViewModel::class.java]

        binding.linearProgress.max = maxCount
        binding.circularProgressIndicator.max = maxCount

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnInc.setOnClickListener { viewModel.increment(maxCount) }
        binding.btnDec.setOnClickListener { viewModel.decrement() }

        observeViewModel()
        viewModel.loadHistory()

        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentCount.collectLatest { count ->
                binding.tvGlassCount.text = "$count of $maxCount Glasses"
                binding.linearProgress.progress = count
                binding.circularProgressIndicator.progress = count
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.history.collectLatest { list ->
                updateChart(list)
            }
        }
    }

//    private fun updateChart(history: List<WaterIntake>) {
//        val entries = history.mapIndexed { index, intake ->
//            BarEntry(index.toFloat(), intake.count.toFloat())
//        }
//        val dataSet = BarDataSet(entries, "Daily Water Intake")
//        val data = BarData(dataSet)
//
//        binding.barChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                history.map { it.date.substring(5) }
//            )
//        binding.barChart.data = data
//        binding.barChart.invalidate()
//    }
//

    private fun updateChart(history: List<WaterIntake>) {
        // Build last 7 dates (MM-dd format)
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val sdfLabel = java.text.SimpleDateFormat("MM-dd", java.util.Locale.getDefault())
        val calendar = java.util.Calendar.getInstance()

        val dateList = (0..6).map {
            val dateStr = sdf.format(calendar.time)
            val label = sdfLabel.format(calendar.time)
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
            dateStr to label
        }.reversed() // oldest to newest

        // Map counts to dates
        val countMap = history.associateBy { it.date }

        val entries = dateList.mapIndexed { index, (fullDate, _) ->
            val count = countMap[fullDate]?.count?.toFloat() ?: 0f
            BarEntry(index.toFloat(), count)
        }

        val dataSet = BarDataSet(entries, "Daily Water Intake").apply {
            color = "#FFE91E63".toColorInt()
            valueTextSize = 12f
        }

        val data = BarData(dataSet).apply {
            barWidth = 0.8f
        }

        binding.barChart.apply {
            this.data = data

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                setLabelCount(7, true)
                valueFormatter = IndexAxisValueFormatter(dateList.map { it.second })
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 6f
                granularity = 1f
                setLabelCount(7, true)
            }

            axisRight.isEnabled = false

            setScaleEnabled(false)
            isDragEnabled = false
            isDoubleTapToZoomEnabled = false
            isHighlightPerTapEnabled = false
            isHighlightPerDragEnabled = false

            description.isEnabled = false
            legend.isEnabled = false

            invalidate()
        }
    }
}