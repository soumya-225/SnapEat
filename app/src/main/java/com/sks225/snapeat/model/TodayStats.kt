package com.sks225.snapeat.model

data class TodayStats(
    val calories: Int,
    val caloriesRequired: Int,
    val protein: Double,
    val proteinRequired: Double,
    val carbs: Double,
    val carbsRequired: Double,
    val fat: Double,
    val fatRequired: Double,
    val fiber: Double,
    val fiberRequired: Double
)
