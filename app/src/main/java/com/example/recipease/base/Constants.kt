package com.example.recipease.base

import com.example.recipease.model.Recipe
import com.example.recipease.model.User

interface Identifiable {
    val id: String
}

object Constants {

    // -----------------------------
    // Firestore collections
    // -----------------------------
    const val RECIPES = "recipes"
    const val TAGS = "tags"
    const val TAGS_DOC_ID = "prod"
    const val USERS = "users"

    // -----------------------------
    // Cloudinary folder mapping
    // -----------------------------
    val folderMap: Map<Class<*>, String> = mapOf(
        Recipe::class.java to RECIPES,
        User::class.java to USERS
    )

    fun folderFor(model: Identifiable): String {
        return folderMap[model::class.java] ?: "misc"
    }
}
