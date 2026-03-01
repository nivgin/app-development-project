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

    fun getRecipeById(id: String): LiveData<Recipe> {
        return database.recipeDao.getRecipeById(id)
    }

    fun getRecipesByUser(userId: String): LiveData<List<Recipe>> {
        return database.recipeDao.getRecipesByUser(userId)
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

    fun deleteRecipe(recipe: Recipe, completion: () -> Unit) {
        firebaseModel.deleteRecipe(recipe.id) {
            executor.execute {
                database.recipeDao.deleteRecipe(recipe)
                completion()
            }
        }
    }

    fun addRecipe(recipe: Recipe, recipeImage: Bitmap, completion: () -> Unit) {
        firebaseModel.addRecipe(recipe) {
            StorageModel.uploadImage(recipe, recipeImage) {
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