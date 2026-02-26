package com.example.recipease.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.recipease.data.models.FirebaseModel
import com.example.recipease.model.Recipe
import java.util.concurrent.Executors
import com.example.recipease.dao.AppLocalDB
import com.example.recipease.dao.AppLocalDbRepository
import com.example.recipease.data.models.CloudinaryStorageModel

class RecipeRepository private constructor() {

    private val firebaseModel = FirebaseModel()
    private val StorageModel = CloudinaryStorageModel()
    private var executor = Executors.newSingleThreadExecutor()

    private val database: AppLocalDbRepository = AppLocalDB.db

    companion object {
        val shared = RecipeRepository()
    }

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return database.recipeDao.getAllRecipes()
    }

    fun refreshRecipes() {
        val lastUpdated = Recipe.lastUpdated

        firebaseModel.getAllRecipes(Recipe.lastUpdated) { fetchedRecipes ->
            executor.execute {
                var time = lastUpdated
                for (recipe in fetchedRecipes) {
                    database.recipeDao.insertRecipes(recipe)
                    recipe.lastUpdated?.let { recipeLastUpdated ->
                        if (time < recipeLastUpdated) {
                            time = recipeLastUpdated
                        }
                    }
                }
                Recipe.lastUpdated = time
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
}