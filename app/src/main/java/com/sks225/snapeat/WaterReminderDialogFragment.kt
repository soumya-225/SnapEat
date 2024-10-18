package com.sks225.snapeat

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class WaterReminderDialogFragment(var title: String, var unit: String) : DialogFragment() {

    private var onInputSubmittedListener: ((String) -> Unit)? = null
    private val initialValue: Int = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_water_reminder_dialog, null)

        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.np_water_dialog)
        val tvUnit = dialogView.findViewById<TextView>(R.id.tv_unit)
        tvUnit.text = unit

        numberPicker.minValue = 1
        numberPicker.maxValue = 50
        numberPicker.wrapSelectorWheel = false
        numberPicker.value = initialValue


        builder.setView(dialogView)
            .setTitle(title)
            .setPositiveButton("OK") { dialog, _ ->
                val selectedValue = numberPicker.value
                onInputSubmittedListener?.invoke(selectedValue.toString())
                dialog.dismiss()
            }

        return builder.create()
    }

    fun setOnInputSubmittedListener(listener: (String) -> Unit) {
        this.onInputSubmittedListener = listener
    }
}
