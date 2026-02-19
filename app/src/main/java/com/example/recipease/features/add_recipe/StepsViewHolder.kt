package com.example.recipease.features.add_recipe

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemStepBinding
import androidx.core.widget.addTextChangedListener

class StepsViewHolder(
    val binding: ItemStepBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        stepText: String,
        stepNumber: Int,
        onDelete: (Int) -> Unit,
        onChanged: (Int, String) -> Unit
    ) {
        binding.tvStepNumber.text = stepNumber.toString()
        binding.etInstruction.setText(stepText)

        binding.etInstruction.addTextChangedListener { editable ->
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onChanged(pos, editable?.toString().orEmpty())
            }
        }

        binding.btnDelete.setOnClickListener {
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(pos)
            }
        }
    }
}
