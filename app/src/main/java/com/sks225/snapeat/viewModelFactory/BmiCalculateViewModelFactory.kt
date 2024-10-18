package com.sks225.snapeat.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sks225.snapeat.repository.BmiRepository
import com.sks225.snapeat.viewModel.BmiCalculateViewModel

class BmiCalculateViewModelFactory(
    private val bmiRepository: BmiRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BmiCalculateViewModel::class.java)) {
            return BmiCalculateViewModel(bmiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}