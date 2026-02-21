package com.example.recipease.features.view_recipe

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemViewIngredientBinding
import com.example.recipease.model.Ingredient

class ViewIngredientViewHolder(
    private val binding: ItemViewIngredientBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: Ingredient) {
        binding.ingredientName.text = ingredient.name
        binding.ingredientAmount.text = ingredient.amount
    }
}

