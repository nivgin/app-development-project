package com.example.recipease.features.view_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.Recipe
import com.example.recipease.model.User

class ViewRecipeViewModel : ViewModel() {

    private val recipeRepo = RecipeRepository.shared
    private val userRepo = UserRepository.shared

    lateinit var currentRecipe: LiveData<Recipe>
        private set

    lateinit var currentUser: LiveData<User?>
        private set

    fun init(recipeId: String, userId: String?) {
        currentRecipe = recipeRepo.getRecipeById(recipeId)
        currentUser = if (!userId.isNullOrBlank()) {
            userRepo.getUserById(userId)
        } else {
            userRepo.getUserById("")
        }
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun refreshUsers() {
        userRepo.refreshUsers()
    }
}
