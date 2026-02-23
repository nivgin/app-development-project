package com.example.recipease.features.profile_page

import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemProfileRecipeCardBinding
import com.example.recipease.model.Recipe

class ProfileRecipeViewHolder(
    private var binding: ItemProfileRecipeCardBinding,
    private val listener: OnProfileRecipeClickListener?
) : RecyclerView.ViewHolder(binding.root) {
    private var recipe: Recipe? = null

    fun bind(recipe: Recipe, position: Int) {
        this.recipe = recipe
        binding.recipeTitle.text = recipe.name

        binding.root.setOnClickListener {
            recipe.let { recipe ->
                listener?.onRecipeClick(recipe, position)
            }
        }
    }
}

