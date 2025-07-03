package com.sks225.snapeat.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.model.WaterIntake
import kotlinx.coroutines.tasks.await

class WaterRepository {
    private val db: DatabaseReference by lazy { Firebase.database.reference }
    private val uid: String? get() = Firebase.auth.currentUser?.uid

    suspend fun saveWaterIntake(count: Int) {
        val userId = uid ?: return
        val date = getTodayDate()
        val intake = WaterIntake(date, count)

        db.child("waterIntake")
            .child(userId)
            .child(date)
            .setValue(intake)
            .await()
    }

    suspend fun loadWaterHistory(): List<WaterIntake> {
        val userId = uid ?: return emptyList()

        val snapshot = db.child("waterIntake")
            .child(userId)
            .get()
            .await()

        return snapshot.children.mapNotNull {
            it.getValue(WaterIntake::class.java)
        }.sortedBy { it.date }
    }

    private fun getTodayDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}
