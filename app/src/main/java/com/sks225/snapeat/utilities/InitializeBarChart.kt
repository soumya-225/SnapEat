package com.sks225.snapeat.utilities

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

fun setupBarChart(chart: BarChart) {
    chart.setDrawBarShadow(false)
    chart.setDrawValueAboveBar(false)
    chart.description.isEnabled = false
    chart.setMaxVisibleValueCount(60)
    chart.setPinchZoom(false)
    chart.setDrawGridBackground(false)

    val xAxis: XAxis = chart.xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.granularity = 1f
    xAxis.labelCount = 7

    //val isAxis: ArrayList<Int>

    val leftAxis: YAxis = chart.axisLeft
    leftAxis.setLabelCount(8, false)
    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
    leftAxis.spaceTop = 15f
    leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

    val rightAxis: YAxis = chart.axisRight
    rightAxis.isEnabled = false
    //rightAxis.setDrawGridLines(false)
    //rightAxis.setLabelCount(8, false)
    //rightAxis.spaceTop = 15f
    //rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
}

fun setData(chart: BarChart, dataList: List<Int>) {
    val values = ArrayList<BarEntry>()
    for ((index, value) in dataList.withIndex()) {
        values.add(BarEntry(index.toFloat(), value.toFloat()))
    }

    val set1: BarDataSet

    if (chart.data != null && chart.data.dataSetCount > 0) {
        set1 = chart.data.getDataSetByIndex(0) as BarDataSet
        set1.values = values
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        set1 = BarDataSet(values, "Data Set Label")
        set1.setDrawIcons(false)

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.9f

        chart.data = data
        chart.invalidate()
    }
}