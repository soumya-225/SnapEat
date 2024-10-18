package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentReportBinding
import com.sks225.snapeat.utilities.bmiCategory
import com.sks225.snapeat.utilities.setUpGauge
import com.sks225.snapeat.viewModel.MainFragmentViewModel

class ReportFragment(private var viewModel: MainFragmentViewModel) : Fragment() {
    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        binding.cvBmi.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calculateBmiFragment)
        }

        viewModel.bmiRecordLiveData.observe(viewLifecycleOwner) { bmiRecord ->
            bmiRecord?.let {
                binding.tvBmi.text = "Your BMI is ${it.bmi} "
                binding.tvBmiCategory.text = "You are ${bmiCategory(it.bmi ?: 0f)}"
                setUpGauge(binding.gaugeCard1, it.bmi!!)
            } ?: run {
                binding.tvBmi.text = "BMI not found"
                binding.tvBmiCategory.text = "Calculate your BMI here!!"
                setUpGauge(binding.gaugeCard1, 0f)
            }
        }

        return binding.root
    }
}