package com.example.recipease.features.profile_page

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.features.recipes_feed.RecipesFeedViewModel
import com.example.recipease.model.Recipe
import com.example.recipease.model.User
import kotlin.getValue

class ProfilePageViewModel : ViewModel(){

    private val userRepo = UserRepository.shared
    private val recipeRepo = RecipeRepository.shared

    val connectedUser: LiveData<User?> = userRepo.connectedUser

    val userRecipes: MediatorLiveData<List<Recipe>> = MediatorLiveData<List<Recipe>>().also { mediator ->
        var currentSource: LiveData<List<Recipe>>? = null

        mediator.addSource(connectedUser) { user ->
            currentSource?.let { mediator.removeSource(it) }
            if (user != null) {
                val source: LiveData<List<Recipe>> = recipeRepo.getRecipesByUser(user.id)
                currentSource = source
                mediator.addSource(source) { recipes -> mediator.value = recipes }
            } else {
                currentSource = null
                mediator.value = emptyList()
            }
        }
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun refreshUser() {
        userRepo.refreshUsers()
    }
}