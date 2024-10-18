package com.sks225.snapeat.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentAgeBinding

class AgeFragment : Fragment() {

    private lateinit var binding: FragmentAgeBinding
    private var age: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgeBinding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            val action = AgeFragmentDirections.actionAgeFragmentToWeightFragment(age)
            findNavController().navigate(action)
        }

        binding.numberPicker.minValue = 5
        binding.numberPicker.maxValue = 100
        binding.numberPicker.value = 10

        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            age = newVal
        }

        return binding.root
    }
}
