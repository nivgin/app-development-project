package com.example.recipease.data.models

import com.example.recipease.base.Constants
import com.example.recipease.model.Recipe
import com.example.recipease.model.Tags
import com.example.recipease.model.User
import com.google.firebase.Firebase
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

    fun getAllRecipes(since: Long, completion: (List<Recipe>) -> Unit) {
        database.collection(Constants.RECIPES)
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
        database.collection(Constants.RECIPES).document(recipe.id)
            .set(recipe.toJson)
            .addOnSuccessListener { documentReference ->
                completion()
            }
            .addOnFailureListener { e ->
                completion()
            }
    }

    fun getAllUsers(since: Long, completion: (List<User>) -> Unit) {
        database.collection(Constants.USERS)
            //.whereGreaterThanOrEqualTo(User.LAST_UPDATED_KEY, Timestamp(since / 1000, 0)) In the future when we actually add caching..
            .get()
            .addOnCompleteListener { result ->
                when (result.isSuccessful) {
                    true -> completion(result.result.map { User.fromJson(it.data) })
                    false -> completion(emptyList())
                }
            }
    }

    fun getUserById(userId: String, completion: (User?) -> Unit) {
        database.collection(Constants.USERS)
            .document(userId)
            .get()
            .addOnCompleteListener { result ->
                if (result.isSuccessful && result.result.exists()) {
                    val data = result.result.data
                    if (data != null) {
                        completion(User.fromJson(data))
                    } else {
                        completion(null)
                    }
                } else {
                    completion(null)
                }
            }
    }


    fun addUser(user: User, completion: () -> Unit) {
        database.collection(Constants.USERS).document(user.id)
            .set(user.toJson)
            .addOnSuccessListener { documentReference ->
                completion()
            }.addOnFailureListener { e ->
                completion()
            }
    }

    fun getTags(since: Long, completion: (Tags?) -> Unit) {
        database.collection(Constants.TAGS)
            .document(Constants.TAGS_DOC_ID)
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