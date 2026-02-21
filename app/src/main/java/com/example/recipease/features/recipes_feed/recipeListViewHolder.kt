package com.example.recipease.features.recipes_feed
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemRecipeCardBinding
import com.example.recipease.model.Recipe
import com.squareup.picasso.Picasso

class recipeListViewHolder (
    private var binding: ItemRecipeCardBinding,
    private val listener: OnRecipeClickListener?
) : RecyclerView.ViewHolder(binding.root) {
    private var recipe: Recipe? = null

    fun bind(recipe: Recipe, position: Int) {
        this.recipe = recipe
        binding.recipeTitle.text = recipe.name
        binding.recipeDescription.text = recipe.description
        binding.recipeTime.text = recipe.time
        binding.recipeDifficulty.text = recipe.difficulty
<<<<<<< HEAD
        binding.recipeAuthor.text = recipe.userId
        recipe.pictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .into(binding.recipeImage)

            binding.root.setOnClickListener {
                    recipe.let { recipe ->
                        listener?.onRecipeClick(recipe, position)
                    }
                }
=======
        binding.recipeAuthor.text = recipe.author

        binding.root.setOnClickListener {

            recipe.let { recipe ->
                listener?.onRecipeClick(recipe, position)
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
            }
        }
    }
}