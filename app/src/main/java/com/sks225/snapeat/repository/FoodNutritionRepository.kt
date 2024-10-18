package com.sks225.snapeat.repository

import com.sks225.snapeat.model.NutrientsRequestBody
import com.sks225.snapeat.network.FoodNutritionApi

class FoodNutritionRepository(private val foodNutritionApi: FoodNutritionApi) {
    suspend fun searchFood(query: String) = foodNutritionApi.searchFood(query)
    suspend fun getFoodNutrients(query: String) = foodNutritionApi.getFoodNutrients(
        NutrientsRequestBody(query)
    )
}