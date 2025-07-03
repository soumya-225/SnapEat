package com.sks225.snapeat.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.model.HealthData
import kotlinx.coroutines.tasks.await

class BmiRepository {

    private val userId: String get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val userRef = userId.let {
        FirebaseDatabase.getInstance().reference.child("users").child(it)
    }
    private val bmiRef = userRef.child("health_data").child("bmi")

    suspend fun saveBmiRecord(bmiRecord: BmiRecord) {
        bmiRef.setValue(bmiRecord).await()
    }

    suspend fun getHealthData(): HealthData? {
        return userRef.child("health_data").get().await().getValue(HealthData::class.java)
    }

    suspend fun getBmiRecord(): BmiRecord? {
        return try {
            val snapshot = bmiRef.get().await()
            snapshot.getValue(BmiRecord::class.java)
        } catch (e: Exception) {
            Log.e("BmiRepository", "Error fetching BMI record", e)
            null
        }
    }
}
