package com.example.recipease.features.add_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.TagsRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.Recipe
import com.example.recipease.model.User

class AddRecipeViewModel : ViewModel() {

    private val tagsRepo = TagsRepository.shared
    private val recipeRepo = RecipeRepository.shared
    private val userRepo = UserRepository.shared

    val tags: LiveData<List<String>> = tagsRepo.getAllTags()

    val connectedUser: LiveData<User?> = userRepo.connectedUser

    fun refreshTags() {
        tagsRepo.refreshTags()
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }
}
