package com.example.recipease.features.view_recipe

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemViewStepBinding

class ViewStepViewHolder(
    private val binding: ItemViewStepBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(step: String, position: Int) {
        binding.stepNumber.text = (position + 1).toString()
        binding.stepText.text = step
    }
}

