package com.sks225.snapeat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.repository.BmiRepository
import kotlinx.coroutines.launch

class BmiCalculateViewModel(private val bmiRepository: BmiRepository) : ViewModel() {
    fun saveBmiRecord(bmiRecord: BmiRecord) {
        viewModelScope.launch {
            bmiRepository.saveBmiRecord(bmiRecord)
        }
    }
}