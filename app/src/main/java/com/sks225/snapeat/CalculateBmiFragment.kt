package com.sks225.snapeat

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sks225.snapeat.databinding.FragmentCalculateBmiBinding
import com.sks225.snapeat.model.BmiRecord
import com.sks225.snapeat.viewModel.BmiCalculateViewModel

class CalculateBmiFragment : Fragment() {
    private lateinit var binding: FragmentCalculateBmiBinding
    private lateinit var viewModel: BmiCalculateViewModel

    private var weight: Int = 55
    private var age: Int = 22
    private var currentProgress: Float = 170f
    private var gender: String? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculateBmiBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[BmiCalculateViewModel::class.java]

        setupUI()
        observeViewModel()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupUI() {
        binding.rlMale.setOnClickListener {
            gender = "Male"
            binding.rlMale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalefocus)
            binding.rlFemale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalenotfocus)
        }

        binding.rlFemale.setOnClickListener {
            gender = "Female"
            binding.rlFemale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalefocus)
            binding.rlMale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.malefemalenotfocus)
        }

        binding.sbHeight.max = 300
        binding.sbHeight.progress = 170
        binding.tvCurrHeight.text = "170"
        binding.sbHeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentProgress = progress.toFloat()
                binding.tvCurrHeight.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.npWeight.minValue = 30
        binding.npWeight.maxValue = 150
        binding.npWeight.value = 55
        binding.npWeight.textColor = Color.WHITE
        binding.npWeight.wrapSelectorWheel = false
        binding.npWeight.setOnValueChangedListener { _, _, newVal -> weight = newVal }

        binding.npAge.minValue = 10
        binding.npAge.maxValue = 100
        binding.npAge.value = 22
        binding.npAge.textColor = Color.WHITE
        binding.npAge.wrapSelectorWheel = false
        binding.npAge.setOnValueChangedListener { _, _, newVal -> age = newVal }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCalculateBmi.setOnClickListener {
            if (gender == null) {
                showToast("Select Your Gender First")
            } else if (currentProgress <= 0) {
                showToast("Select Your Height First")
            } else if (age <= 0) {
                showToast("Age is Incorrect")
            } else if (weight <= 0) {
                showToast("Weight Is Incorrect")
            } else {
                val bmiRecord = BmiRecord(weight, currentProgress, gender!!)
                viewModel.saveBmiRecord(bmiRecord)

                val action =
                    CalculateBmiFragmentDirections.actionCalculateBmiFragmentToBmiResultFragment(
                        currentProgress.toString(), weight.toString(), gender!!
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.saveStatus.observe(viewLifecycleOwner) { (success, message) ->
            if (!success) {
                Toast.makeText(
                    requireContext(), message ?: "Failed to save BMI record", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
