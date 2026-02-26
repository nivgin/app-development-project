package com.example.recipease.features.recipes_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.TagsRepository
import com.example.recipease.model.Recipe

class RecipesFeedViewModel : ViewModel() {

    private val recipeRepo = RecipeRepository.shared
    private val tagsRepo = TagsRepository.shared
    private val recipes: LiveData<List<Recipe>> = recipeRepo.getAllRecipes()
    val tags: LiveData<List<String>> = tagsRepo.getAllTags()
    val displayedRecipes = MediatorLiveData<List<Recipe>>()
    var currentSelectedTags: Set<String> = emptySet()

    init {
        displayedRecipes.addSource(recipes) {
            filterRecipesByTag(currentSelectedTags)
        }
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun refreshTags() {
        tagsRepo.refreshTags()
    }

    fun filterRecipesByTag(selectedTags: Set<String>) {
        currentSelectedTags = selectedTags
        val allRecipes = recipes.value ?: emptyList()

        displayedRecipes.value = if (selectedTags.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                selectedTags.all { tag -> recipe.tags.contains(tag) }
            }
        }
    }
}
