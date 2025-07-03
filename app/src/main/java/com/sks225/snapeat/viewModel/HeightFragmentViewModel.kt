package com.sks225.snapeat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sks225.snapeat.model.HealthData
import com.sks225.snapeat.repository.HealthRepository

class HeightFragmentViewModel : ViewModel() {
    private val repository = HealthRepository()

    private val _saveStatus = MutableLiveData<Pair<Boolean, String?>>()
    val saveStatus: LiveData<Pair<Boolean, String?>> get() = _saveStatus

    fun saveHealthData(healthData: HealthData) {
        repository.saveHealthData(healthData) { success, message ->
            _saveStatus.postValue(Pair(success, message))
        }
    }
}
