package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        binding.redirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_ageFragment)
        }

        return binding.root
    }
}