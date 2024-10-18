package com.sks225.snapeat.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
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
}
