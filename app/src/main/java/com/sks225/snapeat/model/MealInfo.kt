package com.sks225.snapeat.model

data class MealInfo(
    val imageUri: String = "",
    val timeMillis: Long = 0,
    val mealTime: MealTime = MealTime.BREAKFAST,
    val foodName: String = "",
    val foodQuantity: Double = 0.0,
    val foodCalories: Double = 0.0,
    val foodCarbs: Double = 0.0,
    val foodFat: Double = 0.0,
    val foodProtein: Double = 0.0,
    val foodFiber: Double = 0.0,
    val measureUnit: String = "",
    val unitWeightGram: Double = 0.0
)
