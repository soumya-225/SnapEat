package com.sks225.snapeat.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.model.TodayStats
import com.sks225.snapeat.model.User
import com.sks225.snapeat.repository.BmiRepository
import com.sks225.snapeat.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainFragmentViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val bmiRepository: BmiRepository = BmiRepository()
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _bmiRecord = MutableLiveData<BmiRecord?>()
    val bmiRecord: LiveData<BmiRecord?> get() = _bmiRecord

    private val _todayStats = MutableLiveData<TodayStats?>()
    val todayStats: LiveData<TodayStats?> get() = _todayStats

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.getUserData()
                }
                _user.postValue(result)
            } catch (e: Exception) {
                Log.e("MainFragmentViewModel", "Error fetching user data", e)
            }
        }
    }

    fun fetchBmiData() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    bmiRepository.getBmiRecord()
                }
                _bmiRecord.postValue(result)
            } catch (e: Exception) {
                Log.e("MainFragmentViewModel", "Error fetching BMI data", e)
            }
        }
    }

    fun computeTodayStats() {
        viewModelScope.launch {
            try {
                val (meals, healthData) = withContext(Dispatchers.IO) {
                    Pair(userRepository.getMealsData(), bmiRepository.getHealthData()!!)
                }

                val caloriesRequired =
                    ((healthData.weight * 10 + 6.25 * healthData.height - 5 * healthData.age + 5) * 1.46).toInt()
                val proteinRequired = healthData.weight * 0.9
                val carbsRequired = caloriesRequired / 8.0
                val fatRequired = 0.3 * caloriesRequired / 9.0
                val fiberRequired = 14 * caloriesRequired / 1000.0

                var calories = 0.0
                var carbs = 0.0
                var fat = 0.0
                var protein = 0.0
                var fiber = 0.0

                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                for (meal in meals) {
                    val mealDay = Calendar.getInstance().apply {
                        timeInMillis = meal.timeMillis
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis

                    if (mealDay == today) {
                        calories += meal.foodCalories
                        carbs += meal.foodCarbs
                        fat += meal.foodFat
                        protein += meal.foodProtein
                        fiber += meal.foodFiber
                    }
                }

                _todayStats.postValue(
                    TodayStats(
                        calories.toInt(), caloriesRequired,
                        protein, proteinRequired,
                        carbs, carbsRequired,
                        fat, fatRequired,
                        fiber, fiberRequired
                    )
                )
            } catch (e: Exception) {
                Log.e("MainFragmentViewModel", "Error computing today stats", e)
            }
        }
    }
}
