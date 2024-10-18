package com.sks225.snapeat.network

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sks225.snapeat.SnapEatApplication
import com.sks225.snapeat.data.FoodNutritionRepository
import com.sks225.snapeat.data.FoodRecognitionRepository
import com.sks225.snapeat.model.MealInfo
import com.sks225.snapeat.model.MealTime
import com.sks225.snapeat.model.PostSnapModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostSnapViewModel(
    private val foodRecognitionRepository: FoodRecognitionRepository,
    private val foodNutritionRepository: FoodNutritionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostSnapModel())
    val uiState = _uiState.asStateFlow()

    fun getFoodData(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val foodName = foodRecognitionRepository.getFoodNameFromImage(imageUri, context)
                if (foodName.isEmpty()) {
                    _uiState.value =
                        _uiState.value.copy(foodName = "Not Detected", nutritionData = null)
                    return@launch
                }
                getFoodData("apple".replace("_", " "), context)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Internet not connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFoodData(foodName: String, context: Context) {
        viewModelScope.launch {
            try {
                val foodSearchData = foodNutritionRepository.searchFood(foodName)
                if (foodSearchData.common.isEmpty()) {
                    _uiState.value =
                        _uiState.value.copy(foodName = "Not Detected", nutritionData = null)
                    return@launch
                }
                val foodQueryName = foodSearchData.common[0].food_name
                val nutritionData = foodNutritionRepository.getFoodNutrients(foodQueryName)
                _uiState.value = _uiState.value.copy(
                    foodName = foodName,
                    measure = nutritionData.foods[0].alt_measures.indexOfFirst {
                        it.measure == nutritionData.foods[0].serving_unit
                    },
                    measures = nutritionData.foods[0].alt_measures,
                    nutritionData = nutritionData.foods[0]
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Internet not connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setMeasurePosition(altMeasurePosition: Int) {
        _uiState.value = _uiState.value.copy(measure = altMeasurePosition)
    }

    fun setQuantity(quantity: Double?) {
        _uiState.value = _uiState.value.copy(quantity = quantity)
    }

    private fun getMealInfo(timeMillis: Long) = run {
        val state = uiState.value
        val factor =
            state.measures[state.measure].serving_weight / state.nutritionData!!.serving_weight_grams
        MealInfo(
            timeMillis = timeMillis,
            mealTime = state.mealTime,
            foodName = state.foodName,
            foodQuantity = state.quantity ?: 0.0,
            foodCalories = state.nutritionData.nf_calories * factor,
            foodCarbs = state.nutritionData.nf_total_carbohydrate * factor,
            foodFat = state.nutritionData.nf_total_fat * factor,
            foodProtein = state.nutritionData.nf_protein * factor,
            foodFiber = state.nutritionData.nf_dietary_fiber * factor,
            measureUnit = state.measures[state.measure].measure,
            unitWeightGram = state.measures[state.measure].serving_weight
        )
    }

    fun setMealTime(mealTime: MealTime) {
        _uiState.value = _uiState.value.copy(mealTime = mealTime)
    }

    fun saveMealInfo(timeMillis: Long) {
        val mealInfo = getMealInfo(timeMillis)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SnapEatApplication)
                val foodRecognitionRepository = application.container.foodRecognitionRepository
                val foodNutritionRepository = application.container.foodNutritionRepository
                PostSnapViewModel(
                    foodRecognitionRepository = foodRecognitionRepository,
                    foodNutritionRepository = foodNutritionRepository
                )
            }
        }
    }
}