package com.example.recipease.features.recipes_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.TagsRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.Recipe
import com.example.recipease.model.User

class RecipesFeedViewModel : ViewModel() {

    data class RecipeWithUser(
        val recipe: Recipe,
        val user: User?
    )
    private val recipeRepo = RecipeRepository.shared
    private val tagsRepo = TagsRepository.shared

    private val userRepo = UserRepository.shared
    private val recipes: LiveData<List<Recipe>> = recipeRepo.getAllRecipes()
    private val users: LiveData<List<User>> = userRepo.getAllUsers()
    val tags: LiveData<List<String>> = tagsRepo.getAllTags()
    val displayedRecipes = MediatorLiveData<List<RecipeWithUser>>()
    var currentSelectedTags: Set<String> = emptySet()

    init {
        displayedRecipes.addSource(recipes) {
            processRecipes(currentSelectedTags)
        }
        displayedRecipes.addSource(users) {
            processRecipes(currentSelectedTags)
        }
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun refreshUsers() {
        userRepo.refreshUsers()
    }

    fun refreshTags() {
        tagsRepo.refreshTags()
    }

    fun processRecipes(selectedTags: Set<String>) {
        currentSelectedTags = selectedTags
        val allRecipes = recipes.value ?: emptyList()
        val allUsers = users.value ?: emptyList()

        val filtered = if (selectedTags.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                selectedTags.all { tag -> recipe.tags.contains(tag) }
            }
        }

        displayedRecipes.value = filtered.map { recipe ->
            val user = allUsers.find { it.id == recipe.userId }
            RecipeWithUser(recipe, user)
        }
    }
}
