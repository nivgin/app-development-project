package com.example.recipease.features.add_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipease.data.repository.FoodsRepository
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.TagsRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.FoodIdSearchResponse
import com.example.recipease.model.FoodSearchItem
import com.example.recipease.model.Recipe
import com.example.recipease.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRecipeViewModel : ViewModel() {

    private val tagsRepo = TagsRepository.shared
    private val recipeRepo = RecipeRepository.shared
    private val userRepo = UserRepository.shared
    private val foodsRepo = FoodsRepository.shared

    val tags: LiveData<List<String>> = tagsRepo.getAllTags()

    val connectedUser: LiveData<User?> = userRepo.connectedUser

    fun refreshTags() {
        tagsRepo.refreshTags()
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun searchFoods(query: String, onResults: (List<FoodSearchItem>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = foodsRepo.searchFoods(query)
            val items = results?.foods?.food ?: emptyList()
            withContext(Dispatchers.Main) {
                onResults(items)
            }
        }
    }

    fun getFoodById(foodId: String, onResult: (FoodIdSearchResponse?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = foodsRepo.getFoodById(foodId)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}
