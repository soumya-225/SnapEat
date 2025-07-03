package com.sks225.snapeat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sks225.snapeat.repository.AuthRepository

class LoginViewModel : ViewModel() {
    val email = MutableLiveData("")
    val pass = MutableLiveData("")

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> get() = _loginResult

    private val repository = AuthRepository()

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    fun login() {
        val emailVal = email.value.orEmpty()
        val passVal = pass.value.orEmpty()

        val error = validate(emailVal, passVal)
        if (error != null) {
            _loginResult.value = Pair(false, error)
            return
        }

        repository.loginUser(emailVal, passVal) { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }

    private fun validate(email: String, password: String): String? {
        return when {
            email.isBlank() -> "Please provide email"
            !email.matches(emailPattern) -> "Enter a valid email"
            password.isBlank() -> "Please provide password"
            password.length < 6 -> "Password too short"
            else -> null
        }
    }

    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()
}
