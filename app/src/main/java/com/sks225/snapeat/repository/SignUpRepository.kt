package com.sks225.snapeat.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.model.User

class SignUpRepository {
    private val auth = Firebase.auth
    private val database = Firebase.database

    fun registerUser(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser!!.uid
                val user = User(name, email, uid)
                database.getReference("users").child(uid).setValue(user)
                    .addOnCompleteListener { saveTask ->
                        if (saveTask.isSuccessful) {
                            onResult(true, null)
                        } else {
                            onResult(false, saveTask.exception?.message)
                        }
                    }
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }
}
