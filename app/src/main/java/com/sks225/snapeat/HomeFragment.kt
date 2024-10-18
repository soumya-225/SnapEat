package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sks225.snapeat.databinding.FragmentHomeBinding
import com.sks225.snapeat.utilities.setUpFullGauge
import com.sks225.snapeat.viewModel.MainFragmentViewModel
import java.util.Calendar


class HomeFragment(private var viewModel: MainFragmentViewModel) : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private var glassCount: Int = 0
    private var glassCountMax: Int = 6
    private var workoutTime: Int = 0
    private var workoutTimeMax: Int = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        viewModel.user.observe(viewLifecycleOwner) {
            it?.let {
                binding.user = it
                Glide.with(this).load(it.photoUrl).into(binding.ivUser)
            }
        }

        binding.llReward.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_rewardFragment)
        }

//        binding.ivUser.setOnClickListener {
//            navController.navigate(R.id.action_homeFragment_to_profileFragment)
//        }

        binding.cvStats.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_foodLogFragment)
        }

        binding.card3.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_trackWaterFragment)
        }

        binding.card4.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_trackWaterFragment)
        }

        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_preSnapFragment)
        }

        binding.btnAdd1.setOnClickListener {
            glassCount++
            setUpFullGauge(binding.gaugeCard3, glassCount.toFloat(), glassCountMax.toFloat())
        }

        binding.btnLess1.setOnClickListener {
            glassCount--
            setUpFullGauge(binding.gaugeCard3, glassCount.toFloat(), glassCountMax.toFloat())
        }

        binding.btnAdd2.setOnClickListener {
            workoutTime += 10
            setUpFullGauge(binding.gaugeCard4, workoutTime.toFloat(), workoutTimeMax.toFloat())
        }

        binding.btnLess2.setOnClickListener {
            workoutTime -= 10
            setUpFullGauge(binding.gaugeCard4, workoutTime.toFloat(), workoutTimeMax.toFloat())
        }

        binding.tvTrackText.text = "How about tracking your ${getGreeting()} meal?"



        return binding.root
    }

    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        return when (timeOfDay) {
            in 0..11 -> "Morning"
            in 12..17 -> "Afternoon"
            else -> "Evening"
        }
    }
}