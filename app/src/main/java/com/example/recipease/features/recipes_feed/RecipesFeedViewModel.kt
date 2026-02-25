package com.example.recipease.features.recipes_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.Recipe
import com.example.recipease.model.User

data class RecipeWithUser(
    val recipe: Recipe,
    val user: User?
)

class RecipesFeedViewModel : ViewModel() {

    private val recipeRepo = RecipeRepository.shared
    private val userRepo = UserRepository.shared

    val connectedUser: LiveData<User?> = UserRepository.shared.connectedUserLive

    val tags: LiveData<List<String>> = recipeRepo.tags

    private val _feed = MutableLiveData<List<RecipeWithUser>>()
    val feed: LiveData<List<RecipeWithUser>> = _feed

    init {
        recipeRepo.recipes.observeForever { updateFeed() }
        userRepo.users.observeForever { updateFeed() }
    }

    fun loadFeed() {
        recipeRepo.getAllRecipes()
        userRepo.getAllUsers()
    }
    fun loadTags() {
        recipeRepo.getAllTags()
    }

    private fun updateFeed() {
        val recipes = recipeRepo.recipes.value ?: return
        val users = userRepo.users.value ?: return

        val combined = recipes.map { recipe ->
            val user = users.find { it.id == recipe.userId }
            RecipeWithUser(recipe, user)
        }

        _feed.postValue(combined) }
}
