package com.example.recipease.data.repository

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.recipease.data.models.FirebaseModel
import com.example.recipease.model.Recipe
import java.util.concurrent.Executors
import androidx.lifecycle.MutableLiveData
import com.example.recipease.data.models.CloudinaryStorageModel
import com.example.recipease.model.Tags

class RecipeRepository private constructor() {

    private val firebaseModel = FirebaseModel()
    private val StorageModel = CloudinaryStorageModel()
    private var executor = Executors.newSingleThreadExecutor()

    private val _recipes = MutableLiveData<List<Recipe>>(emptyList())
    val recipes: LiveData<List<Recipe>> get() = _recipes
    private val _tags = MutableLiveData<List<String>>(emptyList())
    val tags: LiveData<List<String>> get() = _tags

    companion object {
        val shared = RecipeRepository()
    }



    fun getAllRecipes() {
        firebaseModel.getAllRecipes(Recipe.lastUpdated) { fetchedRecipes ->
            executor.execute {
                var time = Recipe.lastUpdated
                for (recipe in fetchedRecipes) {
                    recipe.lastUpdated?.let { recipeLastUpdated ->
                        if (time < recipeLastUpdated) {
                            time = recipeLastUpdated
                        }
                    }
                }
                Recipe.lastUpdated = time

                _recipes.postValue(fetchedRecipes)
            }
        }
    }

    fun addRecipe(recipe: Recipe, recipeImage: Bitmap, completion: () -> Unit) {
        firebaseModel.addRecipe(recipe) {
            StorageModel.uploadRecipeImage(recipe, recipeImage) {
                pictureUrl ->
                if (!pictureUrl.isNullOrEmpty()) {
                    val recipeCopy = recipe.copy(pictureUrl = pictureUrl)
                    firebaseModel.addRecipe(recipeCopy, completion)
                } else {
                    completion()
                }
            }
        }
    }

    fun getAllTags() {
        firebaseModel.getTags(Tags.lastUpdated) { fetchedTags ->
            executor.execute {
                val lastUpdated = Tags.lastUpdated
                val newUpdated = fetchedTags?.lastUpdated ?: lastUpdated
                Tags.lastUpdated = newUpdated

                _tags.postValue(fetchedTags?.tags)
            }
        }
    }
}