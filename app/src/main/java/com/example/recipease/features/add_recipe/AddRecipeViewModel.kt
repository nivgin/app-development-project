package com.example.recipease.features.add_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.TagsRepository
import com.example.recipease.model.Recipe

class AddRecipeViewModel : ViewModel() {

    private val tagsRepo = TagsRepository.shared

    val tags: LiveData<List<String>> = tagsRepo.getAllTags()

    fun refreshTags() {
        tagsRepo.refreshTags()
    }
}
