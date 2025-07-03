package com.sks225.snapeat.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sks225.snapeat.model.HealthData

class HealthRepository {
    fun saveHealthData(healthData: HealthData, onResult: (Boolean, String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            onResult(false, "User not logged in")
            return
        }

        val healthRef = FirebaseDatabase.getInstance()
            .reference.child("users").child(userId).child("health_data")

        healthRef.setValue(healthData).addOnSuccessListener {
            onResult(true, null)
        }.addOnFailureListener {
            onResult(false, it.message)
        }
    }
}
