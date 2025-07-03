package com.sks225.snapeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sks225.snapeat.databinding.FragmentBmiResultBinding
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.utilities.bmiCategory
import com.sks225.snapeat.utilities.setUpGauge
import com.sks225.snapeat.viewModel.BmiCalculateViewModel

class BmiResultFragment : Fragment() {
    private lateinit var binding: FragmentBmiResultBinding
    private lateinit var viewModel: BmiCalculateViewModel
    private val args by navArgs<BmiResultFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBmiResultBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BmiCalculateViewModel::class.java]

        calculateAndDisplayBmi()

        binding.btnRecalculateBmi.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        return binding.root
    }

    private fun calculateAndDisplayBmi() {
        val heightCm = args.height.toFloatOrNull() ?: 0f
        val weight = args.weight.toFloatOrNull() ?: 0f
        val gender = args.gender

        if (weight <= 0f || heightCm <= 0f) {
            showInvalidInput()
            return
        }

        val heightM = heightCm / 100f

        val bmi = weight / (heightM * heightM)

        val formattedBmi = String.format("%.2f", bmi).toFloat()

        val minWeight = String.format("%.1f", 18.5f * heightM * heightM).toFloat()
        val maxWeight = String.format("%.1f", 24.9f * heightM * heightM).toFloat()

        binding.tvBmi.text = formattedBmi.toString()
        binding.tvBmiCategory.text = "You are ${bmiCategory(formattedBmi)}"
        binding.tvGender.text = gender
        binding.tvWeightRange.text = "Your ideal weight range is $minWeight - $maxWeight Kg."

        setUpGauge(binding.gaugeBmi, formattedBmi)

        viewModel.saveBmiRecord(BmiRecord(weight.toInt(), formattedBmi, gender))
    }

    private fun showInvalidInput() {
        binding.tvBmi.text = "Invalid input"
        binding.tvBmiCategory.text = ""
        binding.tvGender.text = ""
        binding.tvWeightRange.text = ""
        setUpGauge(binding.gaugeBmi, 0f)
    }
}
