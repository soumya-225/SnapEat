package com.sks225.snapeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)

        binding.llReward.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rewardFragment)
        }

        binding.ivUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.cvStats.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodLogFragment)
        }

        binding.card3.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_trackWaterFragment)
        }

        binding.card4.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_trackWaterFragment)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_preSnapFragment)
        }


        return binding.root
    }
}