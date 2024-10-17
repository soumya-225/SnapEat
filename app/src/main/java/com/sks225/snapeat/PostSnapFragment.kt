package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentPostSnapBinding

class PostSnapFragment : Fragment() {
    private lateinit var binding: FragmentPostSnapBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostSnapBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnSave.setOnClickListener {
            navController.navigate(R.id.action_postSnapFragment_to_foodLogFragment)
        }
        return binding.root
    }
}