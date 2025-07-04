package com.sks225.snapeat.repository

import com.sks225.snapeat.network.FoodNutritionApi
import com.sks225.snapeat.network.FoodRecognitionApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val nutritionBaseUrl = "https://www.nutritionix.com"
    private val recognitionApiBaseUrl = "http://anywhereapptest.duckdns.org:5000"

    private val nutritionRetrofit = Retrofit.Builder()
        .baseUrl(nutritionBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val recognitionApiRetrofit = Retrofit.Builder()
        .baseUrl(recognitionApiBaseUrl)
        .build()

    private val foodNutritionApi by lazy { nutritionRetrofit.create(FoodNutritionApi::class.java) }
    private val foodRecognitionApi by lazy { recognitionApiRetrofit.create(FoodRecognitionApi::class.java) }
    val foodNutritionRepository by lazy { FoodNutritionRepository(foodNutritionApi) }
    val foodRecognitionRepository by lazy { FoodRecognitionRepository(foodRecognitionApi) }
    val userRepository by lazy { UserRepository() }
    val bmiRepository by lazy { BmiRepository() }
}