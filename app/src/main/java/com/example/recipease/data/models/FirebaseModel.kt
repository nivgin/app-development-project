package com.example.recipease.data.models

import android.util.Log
import com.example.recipease.model.Recipe
import com.example.recipease.model.Tags
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings

class FirebaseModel {
    private var database = Firebase.firestore

    init {
        database.firestoreSettings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
    }

    private companion object COLLECTIONS {
        const val RECIPES = "recipes"
        const val TAGS = "tags"
        const val TAGS_DOC_ID = "prod"
    }

    fun getAllRecipes(since: Long, completion: (List<Recipe>) -> Unit) {
        database.collection(RECIPES)
            //.whereGreaterThanOrEqualTo(Recipe.LAST_UPDATED_KEY, Timestamp(since / 1000, 0)) In the future when we actually add caching..
            .get()
            .addOnCompleteListener { result ->
                when (result.isSuccessful) {
                    true -> completion(result.result.map { Recipe.fromJson(it.data) })
                    false -> completion(emptyList())
                }
            }
    }

    fun addRecipe(recipe: Recipe, completion: () -> Unit) {
        database.collection(RECIPES).document(recipe.id)
            .set(recipe.toJson)
            .addOnSuccessListener { documentReference ->
                completion()
            }
            .addOnFailureListener { e ->
                completion()
            }
    }

    fun getTags(since: Long, completion: (Tags?) -> Unit) {
        database.collection(TAGS)
            .document(TAGS_DOC_ID)
            .get()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val data = result.result.data ?: emptyMap()
                    completion(Tags.fromJson(data))
                } else {
                    completion(null)
                }
            }
    }
}