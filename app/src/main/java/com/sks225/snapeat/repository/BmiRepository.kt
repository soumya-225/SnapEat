package com.sks225.snapeat.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sks225.snapeat.model.BmiRecord
import kotlinx.coroutines.tasks.await

class BmiRepository {

    private val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private val bmiRef = userId.let {
        FirebaseDatabase.getInstance().reference.child("users").child(it).child("health_data")
            .child("bmi")
    }

    suspend fun saveBmiRecord(bmiRecord: BmiRecord) {
        bmiRef.setValue(bmiRecord).await()
    }

    suspend fun getBmiRecord(): BmiRecord? {
        return try {
            val snapshot = bmiRef.get().await()
            snapshot.getValue(BmiRecord::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
