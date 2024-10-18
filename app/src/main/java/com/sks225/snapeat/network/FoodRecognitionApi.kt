package com.sks225.snapeat.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FoodRecognitionApi {
    @POST("/getFoodNameFromImage")
    @Multipart
    suspend fun getFoodNameFromImage(@Part image: MultipartBody.Part): Response<ResponseBody>
}