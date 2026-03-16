package com.example.recipease.model

import android.content.Context
import android.os.Parcelable
import com.example.recipease.base.Identifiable
import com.example.recipease.base.RecipeaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Recipe (
    @PrimaryKey override val id: String,
    val name: String,
    val description: String,
    val time: String,
    val difficulty: String,
    val servings: Int,
    val userId: String,
    val tags: List<String>,
    val steps: List<String>,
    val ingredients: List<Ingredient>,
    val pictureUrl: String?,
    val notes: String,
    val lastUpdated: Long?,
    val deleted: Boolean = false
) : Parcelable, Identifiable {
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
        const val SERVINGS_KEY = "servings"
        const val DIFFICULTY_KEY = "difficulty"
        const val USERID_KEY = "userId"
        const val TAGS_KEY = "tags"
        const val STEPS_KEY = "steps"
        const val INGREDIENTS_KEY = "ingredients"
        const val PICTURE_URL_KEY = "pictureUrl"

        const val NOTES_KEY = "notes"
        const val LAST_UPDATED_KEY = "lastUpdated"
        const val DELETED_KEY = "deleted"

        fun fromJson(json: Map<String, Any?>): Recipe {
            val id = json[ID_KEY] as String
            val name = json[NAME_KEY] as String
            val description = json[DESCRIPTION_KEY] as String
            val pictureUrl = json[PICTURE_URL_KEY] as String
            val time = json[TIME_KEY] as String
            val servings = (json["servings"] as? Long)?.toInt() ?: 0
            val userId = json[USERID_KEY] as String
            val difficulty = json[DIFFICULTY_KEY] as String
            val tags = json[TAGS_KEY] as List<String>
            val steps = json[STEPS_KEY] as List<String>
            val ingredients = (json[INGREDIENTS_KEY] as List<Map<String, Any>>).map {
                val food = (it["food"] as? Map<String, Any>)?.let { f ->
                    FoodSearchItem(
                        foodId = f["foodId"] as String,
                        foodName = f["foodName"] as String,
                        brandName = f["brandName"] as? String,
                        foodType = f["foodType"] as String,
                        foodDescription = f["foodDescription"] as? String,
                        foodUrl = f["foodUrl"] as? String
                    )
                }
                val serving = (it["serving"] as? Map<String, Any>)?.let { s ->
                    val nc = s["normalizedNutritionalContent"] as Map<String, Any>
                    ModifiedServing(
                        servingType = s["servingType"] as String,
                        normalizedNutritionalContent = NutritionalContent(
                            calories = (nc["calories"] as Number).toDouble(),
                            carbohydrate = (nc["carbohydrate"] as Number).toDouble(),
                            protein = (nc["protein"] as Number).toDouble(),
                            fat = (nc["fat"] as Number).toDouble(),
                            saturatedFat = (nc["saturatedFat"] as? Number)?.toDouble(),
                            polyunsaturatedFat = (nc["polyunsaturatedFat"] as? Number)?.toDouble(),
                            monounsaturatedFat = (nc["monounsaturatedFat"] as? Number)?.toDouble(),
                            transFat = (nc["transFat"] as? Number)?.toDouble(),
                            cholesterol = (nc["cholesterol"] as? Number)?.toDouble(),
                            sodium = (nc["sodium"] as? Number)?.toDouble(),
                            potassium = (nc["potassium"] as? Number)?.toDouble(),
                            fiber = (nc["fiber"] as? Number)?.toDouble(),
                            sugar = (nc["sugar"] as? Number)?.toDouble(),
                            addedSugars = (nc["addedSugars"] as? Number)?.toDouble(),
                            vitaminD = (nc["vitaminD"] as? Number)?.toDouble(),
                            vitaminA = (nc["vitaminA"] as? Number)?.toDouble(),
                            vitaminC = (nc["vitaminC"] as? Number)?.toDouble(),
                            calcium = (nc["calcium"] as? Number)?.toDouble(),
                            iron = (nc["iron"] as? Number)?.toDouble()
                        )
                    )
                }
                Ingredient(
                    amount = (it["amount"] as? Number)?.toDouble() ?: 0.0,
                    food = food,
                    serving = serving
                )
            }
            val timestamp = json[LAST_UPDATED_KEY] as? Timestamp
            val notes = json[NOTES_KEY] as String
            val lastUpdatedLong = timestamp?.toDate()?.time
            val deleted = json[DELETED_KEY] as? Boolean ?: false

            return Recipe(
                id = id,
                name = name,
                description = description,
                pictureUrl = pictureUrl,
                time = time,
                servings = servings,
                userId = userId,
                difficulty = difficulty,
                tags = tags,
                steps = steps,
                ingredients = ingredients,
                notes = notes,
                lastUpdated = lastUpdatedLong,
                deleted = deleted
            )
        }
    }

    val toJson: Map<String, Any?>
        get() = hashMapOf(
            ID_KEY to id,
            NAME_KEY to name,
            DESCRIPTION_KEY to description,
            TIME_KEY to time,
            SERVINGS_KEY to servings,
            DIFFICULTY_KEY to difficulty,
            USERID_KEY to userId,
            TAGS_KEY to tags,
            STEPS_KEY to steps,
            INGREDIENTS_KEY to ingredients,
            PICTURE_URL_KEY to pictureUrl,
            NOTES_KEY to notes,
            LAST_UPDATED_KEY to FieldValue.serverTimestamp(),
            DELETED_KEY to deleted
        )
}
