package com.sks225.snapeat.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.model.MealInfo
import com.sks225.snapeat.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val mealsRef get() = Firebase.database.getReference("users")
        .child(Firebase.auth.currentUser?.uid ?: "")
        .child("meals")

    suspend fun getUserData(): User? {
        return try {
            Firebase.database.getReference("users")
                .child(Firebase.auth.currentUser!!.uid)
                .get()
                .await()
                .getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveMealData(mealInfo: MealInfo) {
        val key = mealsRef.push().key
        key?.let {
            mealsRef.child(it).setValue(mealInfo).await()
        }
    }

    suspend fun getMealsData(): List<MealInfo> {
        val snapshot = mealsRef.get().await()
        return snapshot.children.mapNotNull { it.getValue(MealInfo::class.java) }
    }
}
