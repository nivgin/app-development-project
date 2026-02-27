package com.example.recipease.dao

import androidx.room.Room
import com.example.recipease.base.RecipeaseApp

object AppLocalDB {

    val db: AppLocalDbRepository by lazy {

        val context = RecipeaseApp.appContext
            ?: throw IllegalStateException("Context is null")

        Room.databaseBuilder(
            context = context,
            klass = AppLocalDbRepository::class.java,
            name = "recipes.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}