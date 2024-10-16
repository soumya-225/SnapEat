package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentAgeBinding

class AgeFragment : Fragment() {

    private lateinit var binding: FragmentAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgeBinding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_ageFragment_to_weightFragment)
        }

        return binding.root
    }
}
