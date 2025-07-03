package com.sks225.snapeat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.repository.BmiRepository
import kotlinx.coroutines.launch

class BmiCalculateViewModel() : ViewModel() {
    private val bmiRepository = BmiRepository()

    private val _saveStatus = MutableLiveData<Pair<Boolean, String?>>()
    val saveStatus: LiveData<Pair<Boolean, String?>> get() = _saveStatus

    fun saveBmiRecord(bmiRecord: BmiRecord) {
        viewModelScope.launch {
            try {
                bmiRepository.saveBmiRecord(bmiRecord)
                _saveStatus.postValue(Pair(true, null))
            } catch (e: Exception) {
                _saveStatus.postValue(Pair(false, e.message))
            }
        }
    }
}
