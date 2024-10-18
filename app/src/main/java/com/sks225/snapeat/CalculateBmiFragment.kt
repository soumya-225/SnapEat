package com.sks225.snapeat

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentCalculateBmiBinding

class CalculateBmiFragment : Fragment() {
    private lateinit var binding: FragmentCalculateBmiBinding
    private var weight: Int = 0
    private var age: Int = 0
    var currentProgress: Int = 0
    private var gender: String? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculateBmiBinding.inflate(inflater, container, false)

        binding.rlMale.setOnClickListener {
            binding.rlMale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalefocus)
            binding.rlFemale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalenotfocus)
            gender = "Male";
        }

        binding.rlFemale.setOnClickListener {
            binding.rlFemale.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.malefemalefocus)
            binding.rlMale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalenotfocus)
            gender = "Female"
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sbHeight.max = 300
        binding.sbHeight.progress = 170
        binding.sbHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentProgress = progress
                binding.tvCurrHeight.text = currentProgress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.npWeight.minValue = 30
        binding.npWeight.maxValue = 150
        binding.npWeight.textColor = Color.WHITE
        binding.npWeight.wrapSelectorWheel = false
        binding.npWeight.value = 55
        binding.npWeight.setOnValueChangedListener { _, _, newVal ->
            weight = newVal
        }

        binding.npAge.minValue = 10
        binding.npAge.maxValue = 100
        binding.npAge.textColor = Color.WHITE
        binding.npAge.wrapSelectorWheel = false
        binding.npAge.value = 22
        binding.npAge.setOnValueChangedListener { _, _, newVal ->
            age = newVal
        }

        binding.btnCalculateBmi.setOnClickListener {
            if (gender == "")
                Toast.makeText(requireContext(), "Select Your Gender First", Toast.LENGTH_SHORT)
                    .show()
            else if (currentProgress.toString() == "0")
                Toast.makeText(requireContext(), "Select Your Height First", Toast.LENGTH_SHORT)
                    .show()
            else if (age == 0 || age < 0)
                Toast.makeText(requireContext(), "Age is Incorrect", Toast.LENGTH_SHORT).show()
            else if (weight == 0 || weight < 0)
                Toast.makeText(requireContext(), "Weight Is Incorrect", Toast.LENGTH_SHORT).show()
            else {
                val action =
                    CalculateBmiFragmentDirections.actionCalculateBmiFragmentToBmiResultFragment(
                        weight.toString(),
                        currentProgress.toString(),
                        gender.toString()
                    )
                findNavController().navigate(action)
            }
        }


        return binding.root
    }
}