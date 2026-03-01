package com.example.recipease.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipease.model.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>
    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipeById(id: String): LiveData<Recipe>

    @Query("SELECT * FROM Recipe WHERE userId = :userId")
    fun getRecipesByUser(userId: String): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(vararg Recipes: Recipe)

    @Delete
    fun deleteRecipe(Recipe: Recipe)
}