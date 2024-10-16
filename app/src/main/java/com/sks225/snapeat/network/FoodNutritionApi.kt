package com.sks225.snapeat.network

import com.sks225.snapeat.model.FoodNutrientsModel
import com.sks225.snapeat.model.FoodSearchModel
import com.sks225.snapeat.model.NutrientsRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodNutritionApi {
    @Headers("Referer: https://www.nutritionix.com")
    @GET("track-api/v2/search/instant?branded=false")
    suspend fun searchFood(@Query("query") query: String): FoodSearchModel

    @Headers("Referer: https://www.nutritionix.com")
    @POST("https://www.nutritionix.com/track-api/v2/natural/nutrients")
    suspend fun getFoodNutrients(@Body requestBody: NutrientsRequestBody): FoodNutrientsModel
}