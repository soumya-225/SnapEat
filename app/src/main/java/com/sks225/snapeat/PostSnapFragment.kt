package com.sks225.snapeat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sks225.snapeat.databinding.FragmentPostSnapBinding
import com.sks225.snapeat.model.MealTime
import com.sks225.snapeat.network.PostSnapViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PostSnapFragment : Fragment() {
    private val viewModel by viewModels<PostSnapViewModel> { PostSnapViewModel.Factory }
    private lateinit var binding: FragmentPostSnapBinding
    private lateinit var navController: NavController
    private lateinit var imageUri: Uri
    private val snapTime = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostSnapBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveMealInfo(timeMillis = snapTime)
            navController.popBackStack(R.id.mainFragment, false)
        }

        binding.etQuantity.doAfterTextChanged { text ->
            viewModel.setQuantity(text.toString().toDoubleOrNull())
        }

        imageUri = Uri.parse(navArgs<PostSnapFragmentArgs>().value.imageUri)
        binding.snapImage.setImageURI(imageUri)
        binding.tvTime.text = getTimeString(snapTime)

        binding.spTime.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            MealTime.entries.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        viewModel.setMealTime(
            when (getCurrentHour(snapTime)) {
                in 6..11 -> MealTime.BREAKFAST
                in 12..17 -> MealTime.LUNCH
                else -> MealTime.DINNER
            }
        )

        lifecycleScope.launch {
            viewModel.uiState.collect { data ->
                binding.tvFood.text = data.foodName
                binding.spUnit.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    viewModel.uiState.value.measures.map { it.measure }
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                binding.spUnit.setSelection(data.measure)
                binding.btnSave.isEnabled = data.nutritionData != null && data.quantity != null
                binding.spTime.setSelection(viewModel.uiState.value.mealTime.ordinal)
            }
        }

        viewModel.getFoodData(imageUri, requireContext())

        binding.spUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                viewModel.setMeasurePosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        binding.spTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                viewModel.setMealTime(MealTime.entries[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        return binding.root
    }

    private fun getTimeString(timeMillis: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timeMillis)
        return dateFormat.format(date)
    }

    private fun getCurrentHour(timeMillis: Long): Int {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}