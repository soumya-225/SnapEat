package com.sks225.snapeat.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentHeightBinding
import com.sks225.snapeat.model.HealthData
import com.sks225.snapeat.viewModel.HeightFragmentViewModel

class HeightFragment : Fragment() {

    private lateinit var binding: FragmentHeightBinding
    private lateinit var viewModel: HeightFragmentViewModel
    private var height1: Int = 5
    private var height2: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HeightFragmentViewModel::class.java]

        val args = navArgs<HeightFragmentArgs>().value
        val weight = args.weight
        val age = args.age

        setupNumberPickers(weight, age)
        setupObservers()

        return binding.root
    }

    private fun setupNumberPickers(weight: Float, age: Int) {
        binding.numberPicker1.minValue = 1
        binding.numberPicker1.maxValue = 8
        binding.numberPicker1.value = 5

        binding.numberPicker2.minValue = 0
        binding.numberPicker2.maxValue = 11
        binding.numberPicker2.value = 0

        val computeHeight = {
            val heightInCm = ((height1 * 12 + height2) * 2.54).toInt()
            HealthData(weight, heightInCm, age)
        }

        binding.numberPicker1.setOnValueChangedListener { _, _, newVal ->
            height1 = newVal
        }

        binding.numberPicker2.setOnValueChangedListener { _, _, newVal ->
            height2 = newVal
        }

        binding.btnContinue.setOnClickListener {
            val data = computeHeight()
            viewModel.saveHealthData(data)
        }
    }

    private fun setupObservers() {
        viewModel.saveStatus.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                findNavController().navigate(R.id.action_heightFragment_to_homeFragment)
            } else {
                Snackbar.make(binding.root, message ?: "Failed to save health data", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
