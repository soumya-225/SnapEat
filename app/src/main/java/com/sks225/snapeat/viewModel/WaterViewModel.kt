package com.sks225.snapeat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sks225.snapeat.model.WaterIntake
import com.sks225.snapeat.repository.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterViewModel(private val repository: WaterRepository) : ViewModel() {

    private val _currentCount = MutableStateFlow(0)
    val currentCount: StateFlow<Int> = _currentCount

    private val _history = MutableStateFlow<List<WaterIntake>>(emptyList())
    val history: StateFlow<List<WaterIntake>> = _history

    fun increment(max: Int) {
        if (_currentCount.value < max) {
            _currentCount.value += 1
            save()
        }
    }

    fun decrement() {
        if (_currentCount.value > 0) {
            _currentCount.value -= 1
            save()
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val data = repository.loadWaterHistory()
            _history.value = data
            val today = getTodayDate()
            _currentCount.value = data.find { it.date == today }?.count ?: 0
        }
    }

    private fun save() {
        viewModelScope.launch {
            repository.saveWaterIntake(_currentCount.value)
            loadHistory()
        }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}

class WaterViewModelFactory(private val repository: WaterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaterViewModel(repository) as T
    }
}
