package com.sks225.snapeat.network

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FoodRecognitionApi {
    @POST("/getFoodNameFromImage")
    @Multipart
    suspend fun getFoodNameFromImage(@Part image: MultipartBody.Part): String
}