package com.sks225.snapeat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sks225.snapeat.SnapEatApplication
import com.sks225.snapeat.model.HealthData
import com.sks225.snapeat.model.MealInfo
import com.sks225.snapeat.repository.BmiRepository
import com.sks225.snapeat.repository.UserRepository
import kotlinx.coroutines.launch

class FoodLogViewModel(
    private val userRepository: UserRepository,
    private val bmiRepository: BmiRepository,
): ViewModel() {

    fun getMealsData(callback: (List<MealInfo>, HealthData) -> Unit) {
        viewModelScope.launch {
            try {
                callback(userRepository.getMealsData(), bmiRepository.getHealthData()!!)
            } catch (_: Exception) {
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SnapEatApplication)
                val userRepository = application.container.userRepository
                val bmiRepository = application.container.bmiRepository
                FoodLogViewModel(
                    userRepository = userRepository,
                    bmiRepository = bmiRepository
                )
            }
        }
    }
}