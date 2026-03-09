package com.example.recipease.model

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

// --- Foods Search Response ---

data class FoodsSearchResponse(
    @SerializedName("foods") val foods: FoodsSearchResult
)

data class FoodsSearchResult(
    @SerializedName("food") val food: List<FoodSearchItem>,
    @SerializedName("max_results") val maxResults: String,
    @SerializedName("page_number") val pageNumber: String,
    @SerializedName("total_results") val totalResults: String
)

data class FoodSearchItem(
    @SerializedName("food_id") val foodId: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("brand_name") val brandName: String? = null,
    @SerializedName("food_type") val foodType: String,
    @SerializedName("food_description") val foodDescription: String? = null,
    @SerializedName("food_url") val foodUrl: String? = null
)

data class FoodIdSearchResponse(
    @SerializedName("food") val food: Food
)

data class Food(
    @SerializedName("food_id") val foodId: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("brand_name") val brandName: String? = null,
    @SerializedName("food_type") val foodType: String,
    @SerializedName("food_url") val foodUrl: String? = null,
    @SerializedName("servings") val servings: List<Serving>
)

data class Serving(
    @SerializedName("serving_id") val servingId: String,
    @SerializedName("serving_description") val servingDescription: String,
    @SerializedName("serving_url") val servingUrl: String? = null,
    @SerializedName("metric_serving_amount") val metricServingAmount: String? = null,
    @SerializedName("metric_serving_unit") val metricServingUnit: String? = null,
    @SerializedName("number_of_units") val numberOfUnits: String? = null,
    @SerializedName("measurement_description") val measurementDescription: String? = null,
    @SerializedName("is_default") val isDefault: String? = null,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbohydrate") val carbohydrate: String,
    @SerializedName("protein") val protein: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("saturated_fat") val saturatedFat: String? = null,
    @SerializedName("polyunsaturated_fat") val polyunsaturatedFat: String? = null,
    @SerializedName("monounsaturated_fat") val monounsaturatedFat: String? = null,
    @SerializedName("trans_fat") val transFat: String? = null,
    @SerializedName("cholesterol") val cholesterol: String? = null,
    @SerializedName("sodium") val sodium: String? = null,
    @SerializedName("potassium") val potassium: String? = null,
    @SerializedName("fiber") val fiber: String? = null,
    @SerializedName("sugar") val sugar: String? = null,
    @SerializedName("added_sugars") val addedSugars: String? = null,
    @SerializedName("vitamin_d") val vitaminD: String? = null,
    @SerializedName("vitamin_a") val vitaminA: String? = null,
    @SerializedName("vitamin_c") val vitaminC: String? = null,
    @SerializedName("calcium") val calcium: String? = null,
    @SerializedName("iron") val iron: String? = null
)
