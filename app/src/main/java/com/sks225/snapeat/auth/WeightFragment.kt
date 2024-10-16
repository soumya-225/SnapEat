package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentWeightBinding

class WeightFragment : Fragment() {
    private lateinit var binding: FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeightBinding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_weightFragment_to_heightFragment)
        }

        return binding.root
    }
}
