package com.example.recipease.fragments.recipesFeed

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemTagBinding

class tagsViewHolder(
    private val binding: ItemTagBinding,
    private val selected: MutableSet<Int>,
    private val notifyChange: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: String, position: Int) {
        binding.tagText.text = tag

        // Apply selected state
        binding.tagText.isSelected = selected.contains(position)

        binding.tagText.setOnClickListener {
            if (selected.contains(position)) {
                selected.remove(position)
            } else {
                selected.add(position)
            }

            notifyChange(position)
        }
    }
}
