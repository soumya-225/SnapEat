package com.sks225.snapeat.model

data class MealInfo(
    val timeMillis: Long,
    val mealTime: MealTime,
    val foodName: String,
    val foodQuantity: Double,
    val foodCalories: Double,
    val foodCarbs: Double,
    val foodFat: Double,
    val foodProtein: Double,
    val foodFiber: Double,
    val measureUnit: String,
    val unitWeightGram: Double
)
