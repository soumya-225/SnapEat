package com.sks225.snapeat.data

import com.sks225.snapeat.network.FoodNutritionApi
import com.sks225.snapeat.network.FoodRecognitionApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val nutritionBaseUrl = "https://www.nutritionix.com"
    private val recognitionApiBaseUrl = "https://localhost:5000"

    private val nutritionRetrofit = Retrofit.Builder()
        .baseUrl(nutritionBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val recognitionApiRetrofit = Retrofit.Builder()
        .baseUrl(recognitionApiBaseUrl)
        .build()

    private val foodNutritionApi by lazy { nutritionRetrofit.create(FoodNutritionApi::class.java) }
    private val foodRecognitionApi by lazy { nutritionRetrofit.create(FoodRecognitionApi::class.java) }
    val foodNutritionRepository by lazy { FoodNutritionRepository(foodNutritionApi) }
    val foodRecognitionRepository by lazy { FoodNutritionRepository(foodNutritionApi) }
}