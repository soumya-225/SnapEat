package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sks225.snapeat.databinding.FragmentBmiResultBinding
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.repository.BmiRepository
import com.sks225.snapeat.utilities.bmiCategory
import com.sks225.snapeat.utilities.setUpGauge
import com.sks225.snapeat.viewModel.BmiCalculateViewModel
import com.sks225.snapeat.viewModelFactory.BmiCalculateViewModelFactory


class BmiResultFragment : Fragment() {
    private lateinit var binding: FragmentBmiResultBinding
    private lateinit var viewModel: BmiCalculateViewModel
    var height: Float = 0.0f
    private var weight: Float = 0.0f
    private var bmi: Float = 0.0f
    private lateinit var gender: String
    private var minWeight: Float = 0.0f
    private var maxWeight: Float = 0.0f
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBmiResultBinding.inflate(inflater, container, false)
        navController = findNavController()

        val bmiRepository = BmiRepository()
        val factory = BmiCalculateViewModelFactory(bmiRepository)
        viewModel = ViewModelProvider(this, factory)[BmiCalculateViewModel::class.java]

        weight = navArgs<BmiResultFragmentArgs>().value.weight.toFloat()
        height = navArgs<BmiResultFragmentArgs>().value.height.toFloat()
        gender = navArgs<BmiResultFragmentArgs>().value.gender

        height /= 100
        bmi = weight / (height * height)
        bmi = "%.2f".format(bmi).toFloat()

        minWeight = "%.1f".format(18.5f * height * height).toFloat()
        maxWeight = "%.1f".format(24.9f * height * height).toFloat()

        binding.tvBmiCategory.text = "You are " + bmiCategory(bmi)
        binding.tvBmi.text = bmi.toString()
        binding.tvGender.text = gender
        binding.tvWeightRange.text = "Your ideal weight range is $minWeight - $maxWeight Kg."

        binding.btnRecalculateBmi.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack(R.id.mainFragment, false)
        }

        val bmiRecord = BmiRecord(bmi, gender)
        viewModel.saveBmiRecord(bmiRecord)

        setUpGauge(binding.gaugeBmi, bmi)

        return binding.root
    }
}