package com.example.recipease.fragments.recipesFeed
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemRecipeCardBinding
import com.example.recipease.model.Recipe

class recipeListViewHolder (private var binding: ItemRecipeCardBinding) : RecyclerView.ViewHolder(binding.root) {
    private var recipe: Recipe? = null

    fun bind(recipe: Recipe, position: Int) {
        this.recipe = recipe
        binding.recipeTitle.text = recipe.name
        binding.recipeDescription.text = recipe.description
        binding.recipeTime.text = recipe.time
        binding.recipeDifficulty.text = recipe.difficulty
        binding.recipeAuthor.text = recipe.author
    }
}