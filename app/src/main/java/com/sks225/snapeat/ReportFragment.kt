package com.sks225.snapeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentReportBinding
import com.sks225.snapeat.utilities.bmiCategory
import com.sks225.snapeat.utilities.setUpGauge
import com.sks225.snapeat.viewModel.MainFragmentViewModel

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]

        setupUI()

        observeBmi()

        return binding.root
    }

    private fun setupUI() {
        binding.cvBmi.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calculateBmiFragment)
        }
    }

    private fun observeBmi() {
        viewModel.bmiRecord.observe(viewLifecycleOwner) { bmiRecord ->
            Log.d("ReportFragment", "BMI Record: $bmiRecord ${bmiRecord?.bmi}")
            if (bmiRecord != null && bmiRecord.bmi != null) {
                val bmiValue = String.format("%.2f", bmiRecord.bmi)
                binding.tvBmi.text = "Your BMI is $bmiValue"
                binding.tvBmiCategory.text = "You are ${bmiCategory(bmiRecord.bmi)}"
                setUpGauge(binding.gaugeCard1, bmiRecord.bmi)
            } else {
                binding.tvBmi.text = "BMI not found"
                binding.tvBmiCategory.text = "Calculate your BMI here!!"
                setUpGauge(binding.gaugeCard1, 0f)
            }
        }
    }
}
