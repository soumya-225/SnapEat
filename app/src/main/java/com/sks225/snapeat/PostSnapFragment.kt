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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sks225.snapeat.databinding.FragmentPostSnapBinding
import com.sks225.snapeat.model.MealTime
import com.sks225.snapeat.viewModel.PostSnapViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

class PostSnapFragment : Fragment() {

    private val viewModel by viewModels<PostSnapViewModel> { PostSnapViewModel.Factory }
    private lateinit var binding: FragmentPostSnapBinding
    private val args by navArgs<PostSnapFragmentArgs>()
    private lateinit var imageUri: Uri
    private val snapTime = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostSnapBinding.inflate(inflater, container, false)

        imageUri = args.imageUri.toUri()
        binding.snapImage.setImageURI(imageUri)
        binding.tvTime.text = getTimeString(snapTime)

        setupUI()
        observeUiState()

        viewModel.getFoodData(imageUri, requireContext())

        return binding.root
    }

    private fun setupUI() {
        val navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveMealInfo(imageUri.toString(), snapTime)
            navController.popBackStack(R.id.mainFragment, false)
        }

        binding.etQuantity.doAfterTextChanged { text ->
            viewModel.setQuantity(text.toString().toDoubleOrNull())
        }

        binding.spTime.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            MealTime.entries.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spTime.onItemSelectedListener = SimpleItemSelectedListener { position ->
            viewModel.setMealTime(MealTime.entries[position])
        }

        binding.spUnit.onItemSelectedListener = SimpleItemSelectedListener { position ->
            viewModel.setMeasurePosition(position)
        }

        // Set initial meal time suggestion
        viewModel.setMealTime(
            when (getCurrentHour(snapTime)) {
                in 6..11 -> MealTime.BREAKFAST
                in 12..17 -> MealTime.LUNCH
                else -> MealTime.DINNER
            }
        )
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { data ->
                binding.tvFood.text = data.foodName

                binding.spUnit.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    data.measures.map { it.measure }
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

                if (data.measure in data.measures.indices) {
                    binding.spUnit.setSelection(data.measure)
                }

                binding.spTime.setSelection(data.mealTime.ordinal)

                binding.btnSave.isEnabled = data.nutritionData != null && data.quantity != null
            }
        }
    }

    private fun getTimeString(timeMillis: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Date(timeMillis))
    }

    private fun getCurrentHour(timeMillis: Long): Int {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}

class SimpleItemSelectedListener(
    private val onSelected: (position: Int) -> Unit,
) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View?,
        position: Int,
        id: Long,
    ) = onSelected(position)

    override fun onNothingSelected(parent: AdapterView<*>) = Unit
}
