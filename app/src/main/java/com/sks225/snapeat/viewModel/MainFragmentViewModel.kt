package com.sks225.snapeat.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.repository.BmiRepository
import com.sks225.snapeat.repository.UserRepository
import com.sks225.snapeat.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(
    private val userRepository: UserRepository,
    private val bmiRepository: BmiRepository,
) : ViewModel() {
    init {
        getUser()
        fetchBmiData()
    }

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> get() = _user

    private val _bmiRecordLiveData = MutableLiveData<BmiRecord?>()
    val bmiRecordLiveData: MutableLiveData<BmiRecord?> get() = _bmiRecordLiveData

    fun getUser() {
        viewModelScope.launch {
            try {
                val userResult = withContext(Dispatchers.IO) {
                    userRepository.getUserData()
                }
                _user.value = userResult
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Error fetching user data", e)
            }
        }
    }

    fun fetchBmiData() {
        viewModelScope.launch {
            try {
                val bmiRecord = withContext(Dispatchers.IO) {
                    bmiRepository.getBmiRecord()
                }
                _bmiRecordLiveData.postValue(bmiRecord)
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Error fetching BMI data", e)
            }
        }
    }
}
