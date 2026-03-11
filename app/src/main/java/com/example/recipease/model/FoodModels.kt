package com.example.recipease.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Type

// --- Foods Search Response ---

data class FoodsSearchResponse(
    @SerializedName("foods") val foods: FoodsSearchBody
)

data class FoodsSearchBody(
    @SerializedName("food") val food: List<FoodSearchItem>,
    @SerializedName("max_results") val maxResults: String,
    @SerializedName("page_number") val pageNumber: String,
    @SerializedName("total_results") val totalResults: String
)

@Parcelize
data class FoodSearchItem(
    @SerializedName("food_id") val foodId: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("brand_name") val brandName: String? = null,
    @SerializedName("food_type") val foodType: String,
    @SerializedName("food_description") val foodDescription: String? = null,
    @SerializedName("food_url") val foodUrl: String? = null
) : Parcelable

@Parcelize
data class ModifiedServing(
    val servingType: String,
    val normalizedNutritionalContent: NutritionalContent
) : Parcelable

@Parcelize
data class NutritionalContent(
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double,
    val saturatedFat: Double?,
    val polyunsaturatedFat: Double?,
    val monounsaturatedFat: Double?,
    val transFat: Double?,
    val cholesterol: Double?,
    val sodium: Double?,
    val potassium: Double?,
    val fiber: Double?,
    val sugar: Double?,
    val addedSugars: Double?,
    val vitaminD: Double?,
    val vitaminA: Double?,
    val vitaminC: Double?,
    val calcium: Double?,
    val iron: Double?
) : Parcelable

data class FoodIdSearchResponse(
    @SerializedName("food") val food: Food
)

data class Food(
    @SerializedName("food_id") val foodId: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("brand_name") val brandName: String? = null,
    @SerializedName("food_type") val foodType: String,
    @SerializedName("food_url") val foodUrl: String? = null,
    @SerializedName("servings") val servings: ServingList
)

data class ServingList(
    @SerializedName("serving") val serving: List<Serving>
)

data class Serving(
    @SerializedName("serving_id") val servingId: String,
    @SerializedName("serving_description") val servingDescription: String,
    @SerializedName("serving_url") val servingUrl: String? = null,
    @SerializedName("metric_serving_amount") val metricServingAmount: String? = null,
    @SerializedName("metric_serving_unit") val metricServingUnit: String? = null,
    @SerializedName("number_of_units") val numberOfUnits: Double? = null,
    @SerializedName("measurement_description") val measurementDescription: String? = null,
    @SerializedName("is_default") val isDefault: String? = null,
    @SerializedName("calories") val calories: Double,
    @SerializedName("carbohydrate") val carbohydrate: Double,
    @SerializedName("protein") val protein: Double,
    @SerializedName("fat") val fat: Double,
    @SerializedName("saturated_fat") val saturatedFat: Double? = null,
    @SerializedName("polyunsaturated_fat") val polyunsaturatedFat: Double? = null,
    @SerializedName("monounsaturated_fat") val monounsaturatedFat: Double? = null,
    @SerializedName("trans_fat") val transFat: Double? = null,
    @SerializedName("cholesterol") val cholesterol: Double? = null,
    @SerializedName("sodium") val sodium: Double? = null,
    @SerializedName("potassium") val potassium: Double? = null,
    @SerializedName("fiber") val fiber: Double? = null,
    @SerializedName("sugar") val sugar: Double? = null,
    @SerializedName("added_sugars") val addedSugars: Double? = null,
    @SerializedName("vitamin_d") val vitaminD: Double? = null,
    @SerializedName("vitamin_a") val vitaminA: Double? = null,
    @SerializedName("vitamin_c") val vitaminC: Double? = null,
    @SerializedName("calcium") val calcium: Double? = null,
    @SerializedName("iron") val iron: Double? = null
)
