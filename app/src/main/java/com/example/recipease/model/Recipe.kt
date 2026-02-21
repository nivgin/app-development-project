package com.example.recipease.model

import android.content.Context
import com.example.recipease.base.RecipeaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class Recipe (
    val id: String,
    val name: String,
    val description: String,
    val time: String,
    val difficulty: String,
    val userId: String,
    val tags: List<String>,
    val steps: List<String>,
    val ingredients: List<Ingredient>,
    val pictureUrl: String?,
    val notes: String,
    val lastUpdated: Long?
) {
    companion object {

        var lastUpdated: Long
            get() {
                return RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(LAST_UPDATED_KEY, 0) ?: 0
            }
            set(value) {
                RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.putLong(LAST_UPDATED_KEY, value)
                    ?.apply()
            }

        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val DESCRIPTION_KEY = "description"
        const val TIME_KEY = "time"
        const val DIFFICULTY_KEY = "difficulty"
        const val USERID_KEY = "userId"
        const val TAGS_KEY = "tags"
        const val STEPS_KEY = "steps"
        const val INGREDIENTS_KEY = "ingredients"
        const val PICTURE_URL_KEY = "pictureUrl"

        const val NOTES_KEY = "notes"
        const val LAST_UPDATED_KEY = "lastUpdated"

        fun fromJson(json: Map<String, Any?>): Recipe {
            val id = json[ID_KEY] as String
            val name = json[NAME_KEY] as String
            val description = json[DESCRIPTION_KEY] as String
            val pictureUrl = json[PICTURE_URL_KEY] as String
            val time = json[TIME_KEY] as String
            val userId = json[USERID_KEY] as String
            val difficulty = json[DIFFICULTY_KEY] as String
            val tags = json[TAGS_KEY] as List<String>
            val steps = json[STEPS_KEY] as List<String>
            val ingredients = json[INGREDIENTS_KEY] as List<Ingredient>
            val timestamp = json[LAST_UPDATED_KEY] as? Timestamp
            val notes = json[NOTES_KEY] as String
            val lastUpdatedLong = timestamp?.toDate()?.time

            return Recipe(
                id = id,
                name = name,
                description = description,
                pictureUrl = pictureUrl,
                time = time,
                userId = userId,
                difficulty = difficulty,
                tags = tags,
                steps = steps,
                ingredients = ingredients,
                notes = notes,
                lastUpdated = lastUpdatedLong
            )
        }
    }

    val toJson: Map<String, Any?>
        get() = hashMapOf(
            ID_KEY to id,
            NAME_KEY to name,
            DESCRIPTION_KEY to description,
            TIME_KEY to time,
            DIFFICULTY_KEY to difficulty,
            USERID_KEY to userId,
            TAGS_KEY to tags,
            STEPS_KEY to steps,
            INGREDIENTS_KEY to ingredients,
            PICTURE_URL_KEY to pictureUrl,
            NOTES_KEY to notes,
            LAST_UPDATED_KEY to FieldValue.serverTimestamp()
        )
}