package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentHeightBinding
import com.sks225.snapeat.model.HealthData
import com.sks225.snapeat.viewModel.HeightFragmentViewModel

class HeightFragment : Fragment() {
    private lateinit var binding: FragmentHeightBinding
    private var height: Int = 0
    private var height1: Int = 0
    private var height2: Int = 0
    private lateinit var healthData: HealthData
    private lateinit var viewModel: HeightFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HeightFragmentViewModel::class.java]

        val weight = navArgs<HeightFragmentArgs>().value.weight
        val age = navArgs<HeightFragmentArgs>().value.age

        binding.btnContinue.setOnClickListener{
            viewModel.saveHealthData(healthData)
            findNavController().navigate(R.id.action_heightFragment_to_homeFragment)
        }

        binding.numberPicker1.minValue = 1
        binding.numberPicker1.maxValue = 8
        binding.numberPicker1.value = 5

        binding.numberPicker2.minValue = 0
        binding.numberPicker2.maxValue = 11
        binding.numberPicker2.value = 0

        binding.numberPicker1.setOnValueChangedListener { _, _, newVal ->
            height1 = newVal
            height = ((height1 * 12 + height2) * 2.54).toInt()
        }

        binding.numberPicker2.setOnValueChangedListener { _, _, newVal ->
            height2 = newVal
            height = ((height1 * 12 + height2) * 2.54).toInt()
        }



        healthData = HealthData(weight, height,age)

        return binding.root
    }
}
