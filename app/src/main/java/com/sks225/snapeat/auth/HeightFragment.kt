package com.sks225.snapeat.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentHeightBinding

class HeightFragment : Fragment() {
    private lateinit var binding: FragmentHeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeightBinding.inflate(inflater, container, false)

        return binding.root
    }
}
