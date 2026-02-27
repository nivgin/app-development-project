package com.example.recipease.features.recipes_feed
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemRecipeCardBinding
import com.example.recipease.model.Recipe
import com.squareup.picasso.Picasso
import com.example.recipease.features.recipes_feed.RecipesFeedViewModel.RecipeWithUser

class recipeListViewHolder (
    private var binding: ItemRecipeCardBinding,
    private val listener: OnRecipeClickListener?
) : RecyclerView.ViewHolder(binding.root) {
    private var recipe: Recipe? = null

    fun bind(post: RecipeWithUser, position: Int) {
        this.recipe = post.recipe
        binding.recipeTitle.text = post.recipe.name
        binding.recipeDescription.text = post.recipe.description
        binding.recipeTime.text = post.recipe.time
        binding.recipeDifficulty.text = post.recipe.difficulty
        binding.recipeAuthor.text = post.user?.displayName
        post.user?.profilePictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .into(binding.authorImage)
            }
        }
        post.recipe.pictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .into(binding.recipeImage)

            binding.root.setOnClickListener {
                    recipe.let { recipe ->
                        listener?.onRecipeClick(post.recipe, position)
                    }
                }
            }
        }
    }
}