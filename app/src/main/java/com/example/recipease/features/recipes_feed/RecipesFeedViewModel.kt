package com.example.recipease.features.recipes_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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
    var currentSearchFilter: String = ""
    val connectedUser: LiveData<User?> = userRepo.connectedUser

    init {
        displayedRecipes.addSource(recipes) {
            processRecipes(currentSelectedTags, currentSearchFilter)
        }
        displayedRecipes.addSource(users) {
            processRecipes(currentSelectedTags, currentSearchFilter)
        }
    }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var loadingCounter = 0

    private fun startLoading() {
        if (loadingCounter == 0) {
            _isLoading.value = true
        }
        loadingCounter++
    }

    private fun stopLoading() {
        loadingCounter--
        if (loadingCounter <= 0) {
            loadingCounter = 0
            _isLoading.value = false
        }
    }

    fun refreshRecipes() {
        startLoading()
        recipeRepo.refreshRecipes() {
            stopLoading()
        }
    }

    fun refreshUsers() {
        startLoading()
        userRepo.refreshUsers() {
            stopLoading()
        }
    }

    fun refreshTags() {
        startLoading()
        tagsRepo.refreshTags() {
            stopLoading()
        }
    }

    fun processRecipes(selectedTags: Set<String>, searchFilter: String) {
        currentSelectedTags = selectedTags
        currentSearchFilter = searchFilter

        val allRecipes = recipes.value ?: emptyList()
        val allUsers = users.value ?: emptyList()

        val filtered = if (selectedTags.isEmpty() && searchFilter.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                // Check tags
                val matchesTags = selectedTags.all { tag -> recipe.tags.contains(tag) }

                // Check search filter (case-insensitive)
                val matchesSearch = searchFilter.isEmpty() ||
                        recipe.name.contains(searchFilter, ignoreCase = true)

                matchesTags && matchesSearch
            }
        }

        displayedRecipes.value = filtered.map { recipe ->
            val user = allUsers.find { it.id == recipe.userId }
            RecipeWithUser(recipe, user)
        }
    }
}
