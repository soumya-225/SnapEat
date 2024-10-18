package com.sks225.snapeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentFoodLogBinding
import com.sks225.snapeat.databinding.FragmentProfileBinding

class FoodLogFragment : Fragment() {
    private lateinit var binding: FragmentFoodLogBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodLogBinding.inflate(layoutInflater,container,false)
        navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnAddBreakfast.setOnClickListener {
            navController.navigate(R.id.action_foodLogFragment_to_preSnapFragment)
        }

        binding.btnAddLunch.setOnClickListener {
            navController.navigate(R.id.action_foodLogFragment_to_preSnapFragment)
        }

        binding.btnAddSnack.setOnClickListener {
            navController.navigate(R.id.action_foodLogFragment_to_preSnapFragment)
        }

        binding.btnAddDinner.setOnClickListener {
            navController.navigate(R.id.action_foodLogFragment_to_preSnapFragment)
        }

        return binding.root
    }
}