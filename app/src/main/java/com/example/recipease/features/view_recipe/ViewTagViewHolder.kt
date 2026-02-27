package com.example.recipease.features.view_recipe

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemTagBinding

class ViewTagViewHolder(
    private val binding: ItemTagBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: String) {
        binding.tagText.text = tag
    }
}

