package com.sks225.snapeat.repository

import android.content.Context
import android.net.Uri
import com.sks225.snapeat.network.FoodRecognitionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class FoodRecognitionRepository(private val foodRecognitionApi: FoodRecognitionApi) {
    suspend fun getFoodNameFromImage(imageUri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val mimeType = context.contentResolver.getType(imageUri)
        val file = File(context.cacheDir, "temp_image")
        withContext(Dispatchers.IO) {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
        }
        val multipartBody = MultipartBody.Part.createFormData(
            "file", "image", file.asRequestBody(mimeType?.toMediaTypeOrNull())
        )
        val response = foodRecognitionApi.getFoodNameFromImage(multipartBody)
        return response.body()?.string() ?: ""
    }
}