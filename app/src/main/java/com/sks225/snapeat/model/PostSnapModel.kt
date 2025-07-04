package com.sks225.snapeat.model

data class PostSnapModel(
    val foodName: String = "Identifying",
    val measure: Int = 0,
    val measures: List<AltMeasure> = listOf(
        AltMeasure(
            measure = "Detecting",
            qty = 0.0,
            seq = 0,
            serving_weight = 0.0
        )
    ),
    val quantity: Double? = null,
    val mealTime: MealTime = MealTime.BREAKFAST,
    val nutritionData: Food? = null
)
