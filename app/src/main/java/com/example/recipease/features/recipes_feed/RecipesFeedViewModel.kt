package com.example.recipease.features.recipes_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.model.Recipe

class RecipesFeedViewModel : ViewModel() {

    private val repo = RecipeRepository.shared

    val recipes: LiveData<List<Recipe>> = repo.recipes

    val tags: LiveData<List<String>> = repo.tags

    fun loadRecipes() {
        repo.getAllRecipes()
    }

    fun loadTags() {
        repo.getAllTags()
    }
}
