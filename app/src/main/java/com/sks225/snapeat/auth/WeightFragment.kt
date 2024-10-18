package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shawnlin.numberpicker.NumberPicker
import com.sks225.snapeat.databinding.FragmentWeightBinding

class WeightFragment : Fragment() {
    private lateinit var binding: FragmentWeightBinding
    private lateinit var np1: NumberPicker
    private lateinit var np2: NumberPicker
    private var weight: Double = 0.0
    private var weight1: Int = 0
    private var weight2: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        np1 = binding.numberPicker1
        np2 = binding.numberPicker2

        val age = navArgs<WeightFragmentArgs>().value.age


        binding.btnNext.setOnClickListener {
            val action = WeightFragmentDirections.actionWeightFragmentToHeightFragment(weight.toFloat(), age)
            findNavController().navigate(action)
        }

        np1.minValue = 10
        np1.maxValue = 150
        np1.value = 40

        np2.minValue = 0
        np2.maxValue = 9
        np2.value = 0

        np1.setOnValueChangedListener { _, _, newVal ->
            weight1 = newVal
            weight = weight1 + 0.1 * weight2
        }

        np2.setOnValueChangedListener { _, _, newVal ->
            weight2 = newVal
            weight = weight1 + 0.1 * weight2
        }

        return binding.root
    }
}
