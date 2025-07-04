package com.sks225.snapeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sks225.snapeat.adapter.FoodLogAdapter
import com.sks225.snapeat.databinding.FragmentFoodLogBinding
import com.sks225.snapeat.viewModel.FoodLogViewModel
import java.util.Calendar
import kotlin.math.roundToInt

class FoodLogFragment : Fragment() {
    private lateinit var binding: FragmentFoodLogBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<FoodLogViewModel> { FoodLogViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFoodLogBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        getTodayStats()

        return binding.root
    }

    private fun isToday(timeMillis: Long): Boolean {
        // Get an instance of the Calendar
        val calendar = Calendar.getInstance()

        // Get today's date (midnight, i.e., time part set to 0)
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // Get the date from the provided timeMillis (without time part)
        calendar.timeInMillis = timeMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Compare the two dates
        return today.timeInMillis == calendar.timeInMillis
    }

    private fun getTodayStats() {
        viewModel.getMealsData { meals, healthData ->
            Log.d("Test", healthData.toString())
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
            for (meal in meals) {
                if (!isToday(meal.timeMillis))
                    continue

                calories += meal.foodCalories
                carbs += meal.foodCarbs
                fat += meal.foodFat
                protein += meal.foodProtein
                fiber += meal.foodFiber
            }

            binding.rvTodayFoods.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvTodayFoods.adapter = FoodLogAdapter(meals)

            binding.tvCal.text = "${calories.toInt()} / $caloriesRequired"
            binding.tvCarb.text = "Carbs: ${carbs.toInt()} g / ${carbsRequired.toInt()} g"
            binding.tvProtein.text = "Protein: ${protein.toInt()} g / ${proteinRequired.toInt()} g"
            binding.tvFat.text = "Fat: ${fat.toInt()} g / ${fatRequired.toInt()} g"
            binding.tvFiber.text = "Fiber: ${fiber.toInt()} g / ${fiberRequired.toInt()} g"

            binding.progressCircular.progress = (calories * 100 / caloriesRequired).roundToInt()
            binding.linearProgressCarb.progress = (carbs * 100 / carbsRequired).roundToInt()
            binding.linearProgressProtein.progress = (protein * 100 / proteinRequired).roundToInt()
            binding.linearProgressFat.progress = (fat * 100 / fatRequired).roundToInt()
            binding.linearProgressFiber.progress = (fat * 100 / fiberRequired).roundToInt()
        }
    }
}