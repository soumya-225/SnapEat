package com.sks225.snapeat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sks225.snapeat.repository.SignUpRepository

class SignUpViewModel : ViewModel() {

    val email = MutableLiveData("")
    val pass = MutableLiveData("")
    val confirmPass = MutableLiveData("")
    val name = MutableLiveData("")

    private val _signUpStatus = MutableLiveData<Pair<Boolean, String?>>()
    val signUpStatus: LiveData<Pair<Boolean, String?>> get() = _signUpStatus

    private val repository = SignUpRepository()

    fun signUp() {
        val emailVal = email.value.orEmpty()
        val passVal = pass.value.orEmpty()
        val confirmVal = confirmPass.value.orEmpty()
        val nameVal = name.value.orEmpty()

        val error = validateForm(emailVal, passVal, confirmVal, nameVal)
        if (error != null) {
            _signUpStatus.postValue(Pair(false, error))
            return
        }

        repository.registerUser(nameVal, emailVal, passVal) { success, message ->
            _signUpStatus.postValue(Pair(success, message))
        }
    }

    private fun validateForm(
        email: String,
        pass: String,
        confirm: String,
        name: String
    ): String? {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

        return when {
            email.isBlank() -> "Please provide email"
            !email.matches(emailPattern) -> "Enter a valid email"
            name.isBlank() -> "Please provide your name"
            pass.length < 6 -> "Password too short"
            pass != confirm -> "Passwords do not match"
            else -> null
        }
    }
}
