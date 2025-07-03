package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sks225.snapeat.adapter.SuggestedFoodAdapter
import com.sks225.snapeat.databinding.FragmentHomeBinding
import com.sks225.snapeat.model.TodayStats
import com.sks225.snapeat.repository.WaterRepository
import com.sks225.snapeat.utilities.carbFood
import com.sks225.snapeat.utilities.fiberFood
import com.sks225.snapeat.utilities.proteinFood
import com.sks225.snapeat.viewModel.MainFragmentViewModel
import com.sks225.snapeat.viewModel.WaterViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var waterViewModel: WaterViewModel
    private val glassCountMax = 6
    private var workoutTime = 0
    private val workoutTimeMax = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]
        waterViewModel = WaterViewModel(WaterRepository())

        setupUI()
        observeData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.computeTodayStats()
        waterViewModel.loadHistory()
    }

    private fun setupUI() {
        val navController = findNavController()

        binding.fab.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_preSnapFragment) }
        binding.cvStats.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_foodLogFragment) }
        binding.card3.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_trackWaterFragment) }

        binding.btnAdd1.setOnClickListener {
            waterViewModel.increment(glassCountMax)
        }

        binding.btnLess1.setOnClickListener {
            waterViewModel.decrement()
        }

        binding.btnAdd2.setOnClickListener {
            workoutTime += 10
            binding.progressCircularWorkout.progress = workoutTime
            binding.progressCircularWorkout.max = workoutTimeMax
        }

        binding.btnLess2.setOnClickListener {
            workoutTime -= 10
            binding.progressCircularWorkout.progress = workoutTime
            binding.progressCircularWorkout.max = workoutTimeMax
        }

        binding.tvTrackText.text = "How about tracking your ${getGreeting()} meal?"
        binding.tvHi.text = "Good ${getGreeting()}, "
    }

    private fun observeData() {
        viewModel.user.observe(viewLifecycleOwner) { binding.user = it }

        viewModel.todayStats.observe(viewLifecycleOwner) { stats ->
            stats?.let {
                binding.tvCal.text = "${it.calories} of ${it.caloriesRequired}"
                binding.tvCarb.text = "Carbs: ${(it.carbs * 100 / it.carbsRequired).toInt()} %"
                binding.tvProtein.text =
                    "Protein: ${(it.protein * 100 / it.proteinRequired).toInt()} %"
                binding.tvFat.text = "Fat: ${(it.fat * 100 / it.fatRequired).toInt()} %"
                binding.tvFiber.text = "Fiber: ${(it.fiber * 100 / it.fiberRequired).toInt()} %"

                binding.progressCircular.progress = (it.calories * 100 / it.caloriesRequired)
                binding.linearProgressCarb.progress = (it.carbs * 100 / it.carbsRequired).toInt()
                binding.linearProgressProtein.progress =
                    (it.protein * 100 / it.proteinRequired).toInt()
                binding.linearProgressFat.progress = (it.fat * 100 / it.fatRequired).toInt()
                binding.linearProgressFiber.progress = (it.fiber * 100 / it.fiberRequired).toInt()

                loadSuggestion(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            waterViewModel.currentCount.collectLatest { count ->
                binding.progressCircularWater.progress = count
                binding.progressCircularWater.max = glassCountMax
                binding.tvWater.text = "$count of $glassCountMax Glasses"
            }
        }
    }

    private fun loadSuggestion(stats: TodayStats) {
        val suggestion = when {
            stats.protein < stats.carbs && stats.protein < stats.fiber -> "Protein"
            stats.fiber < stats.protein && stats.fiber < stats.carbs -> "Fiber"
            else -> "Carbs"
        }

        val foodList = when (suggestion) {
            "Protein" -> proteinFood
            "Fiber" -> fiberFood
            else -> carbFood
        }

        binding.tvFoodSuggestion.text =
            "It seems you had less $suggestion food\nHere are some $suggestion rich foods..."
        binding.rvFood.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFood.adapter = SuggestedFoodAdapter(foodList)
    }

    private fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Morning"
            in 12..17 -> "Afternoon"
            else -> "Evening"
        }
    }
}
