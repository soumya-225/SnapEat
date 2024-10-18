package com.sks225.snapeat.utilities

import android.graphics.Color
import com.ekn.gruzer.gaugelibrary.FullGauge
import com.ekn.gruzer.gaugelibrary.HalfGauge

fun setUpGauge(halfGauge: HalfGauge, bmi: Float) {
    val range1 = com.ekn.gruzer.gaugelibrary.Range()
    range1.color = Color.parseColor("#00FFFF")
    range1.from =10.0
    range1.to = 18.5

    val range2 = com.ekn.gruzer.gaugelibrary.Range()
    range2.color = Color.parseColor("#00FF00")
    range2.from = 18.5
    range2.to = 24.9

    val range3 = com.ekn.gruzer.gaugelibrary.Range()
    range3.color = Color.parseColor("#FFFF00")
    range3.from = 24.9
    range3.to = 29.9

    val range4 = com.ekn.gruzer.gaugelibrary.Range()
    range4.color = Color.parseColor("#FF0000")
    range4.from = 29.9
    range4.to = 39.9

    halfGauge.addRange(range1)
    halfGauge.addRange(range2)
    halfGauge.addRange(range3)
    halfGauge.addRange(range4)

    halfGauge.minValue = 10.0
    halfGauge.maxValue = 39.9
    halfGauge.value = "%.2f".format(bmi).toDouble()

    halfGauge.setNeedleColor(Color.DKGRAY)
    halfGauge.valueColor = Color.WHITE
    halfGauge.minValueTextColor = Color.TRANSPARENT
    halfGauge.maxValueTextColor = Color.TRANSPARENT
}

fun setUpFullGauge(fullGauge: FullGauge, weight: Float, tweight:Float) {
    /*val range1 = com.ekn.gruzer.gaugelibrary.Range()
    range1.color = Color.parseColor("#00FFFF")
    range1.from =0.0
    range1.to = 18.5

    val range2 = com.ekn.gruzer.gaugelibrary.Range()
    range2.color = Color.parseColor("#00FF00")
    range2.from = 18.5
    range2.to = 24.9

    val range3 = com.ekn.gruzer.gaugelibrary.Range()
    range3.color = Color.parseColor("#FFFF00")
    range3.from = 24.9
    range3.to = 29.9

    val range4 = com.ekn.gruzer.gaugelibrary.Range()
    range4.color = Color.parseColor("#FF0000")
    range4.from = 29.9
    range4.to = 39.9

    halfGauge.addRange(range1)
    halfGauge.addRange(range2)
    halfGauge.addRange(range3)
    halfGauge.addRange(range4)*/

    fullGauge.minValue = 0.0
    fullGauge.maxValue = tweight.toDouble()
    fullGauge.value = weight.toDouble()

    fullGauge.setNeedleColor(Color.DKGRAY)
    fullGauge.valueColor = Color.BLACK
    //fullGauge.minValueTextColor = Color.TRANSPARENT
    //fullGauge.maxValueTextColor = Color.TRANSPARENT
}