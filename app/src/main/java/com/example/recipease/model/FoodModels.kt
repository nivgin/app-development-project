package com.example.recipease.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("food_response")
    val foodResponse: List<FoodItem>
)

data class FoodItem(
    @SerializedName("food_id")
    val foodId: Long,

    @SerializedName("food_entry_name")
    val foodEntryName: String,

    @SerializedName("eaten")
    val eaten: EatenFood
)

data class EatenFood(
    @SerializedName("food_name_singular")
    val foodNameSingular: String,

    @SerializedName("units")
    val units: Double,

    @SerializedName("total_nutritional_content")
    val totalNutritionalContent: TotalNutritionalContent
)

data class TotalNutritionalContent(
    @SerializedName("calories")
    val calories: String,

    @SerializedName("carbohydrate")
    val carbohydrate: String,

    @SerializedName("protein")
    val protein: String,

    @SerializedName("fat")
    val fat: String,

    @SerializedName("saturated_fat")
    val saturatedFat: String,

    @SerializedName("polyunsaturated_fat")
    val polyunsaturatedFat: String,

    @SerializedName("monounsaturated_fat")
    val monounsaturatedFat: String,

    @SerializedName("cholesterol")
    val cholesterol: String,

    @SerializedName("sodium")
    val sodium: String,

    @SerializedName("potassium")
    val potassium: String,

    @SerializedName("fiber")
    val fiber: String,

    @SerializedName("sugar")
    val sugar: String,

    @SerializedName("vitamin_a")
    val vitaminA: String,

    @SerializedName("vitamin_c")
    val vitaminC: String,

    @SerializedName("calcium")
    val calcium: String,

    @SerializedName("iron")
    val iron: String
)
