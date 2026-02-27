package com.example.recipease.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipease.model.Recipe
import com.example.recipease.model.Tags
import com.example.recipease.model.User

@Database(entities = [Recipe::class, Tags::class, User::class], version = 5)
@TypeConverters(Converters::class)
abstract class AppLocalDbRepository: RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val tagsDao: TagsDao

    abstract val userDao: UserDao
}