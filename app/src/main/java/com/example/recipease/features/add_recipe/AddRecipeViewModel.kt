package com.example.recipease.features.add_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.model.Recipe

class AddRecipeViewModel : ViewModel() {

    private val repo = RecipeRepository.shared

    val tags: LiveData<List<String>> = repo.tags

    fun loadTags() {
        repo.getAllTags()
    }
}
