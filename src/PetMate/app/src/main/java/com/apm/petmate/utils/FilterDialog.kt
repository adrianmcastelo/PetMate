package com.apm.petmate.utils

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.apm.petmate.databinding.DialogFilterBinding
import com.google.android.material.chip.Chip

class FilterDialog(
    private val onSubmitClickListener: (type: String, age: String, state: String) -> Unit
): DialogFragment() {

    private lateinit var binding : DialogFilterBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFilterBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.buttonApplyFilter.setOnClickListener {

            var ageChip = binding.ageChipGroup
            val ageChipId = ageChip.checkedChipId
            val selectedAgeChip = ageChip.findViewById<Chip>(ageChipId)?.text.toString()
            var ageValue: String = ""
            when (selectedAgeChip) {
                "CACHORRO" -> ageValue = "CA"
                "ADULTO" -> ageValue = "AD"
                "SENIOR" -> ageValue = "SN"
            }

            var typeChip = binding.typeChipGroup
            val typeChipId = typeChip.checkedChipId
            val selectedTypeChip = typeChip.findViewById<Chip>(typeChipId)?.text.toString()
            var typeValue: String = ""
            if (selectedTypeChip != "null") {
                typeValue = selectedTypeChip
            }

            var stateChip = binding.estadoChipGroup
            val stateChipId = stateChip.checkedChipId
            val selectedStateChip = stateChip.findViewById<Chip>(stateChipId)?.text.toString()
            var stateValue: String = ""
            when (selectedStateChip) {
                "EN ADOPCION" -> stateValue = "DP"
                "ADOPTADO" -> stateValue = "AD"
            }

            onSubmitClickListener.invoke(typeValue, ageValue, stateValue)
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.TOP)
        return dialog
    }
}