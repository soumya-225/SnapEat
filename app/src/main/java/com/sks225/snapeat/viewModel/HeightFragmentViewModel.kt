package com.sks225.snapeat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sks225.snapeat.model.HealthData

class HeightFragmentViewModel : ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val healthRef =
        FirebaseDatabase.getInstance().reference.child("users").child(userId).child("health_data")

    fun saveHealthData(healthData: HealthData) {
        healthRef.setValue(healthData).addOnSuccessListener {
            Log.d("HeightFragmentViewModel", "Health data saved successfully")
        }.addOnFailureListener {
            Log.e("HeightFragmentViewModel", "Error saving health data", it)
        }
    }
}