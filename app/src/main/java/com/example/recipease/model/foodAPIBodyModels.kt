package com.example.recipease.model

import com.google.gson.annotations.SerializedName

data class FreeLanguageBody(
    @SerializedName("user_input")
    val userInput: String,

    @SerializedName("include_food_data")
    val includeFoodData: Boolean = false,

    @SerializedName("eaten_foods")
    val eatenFoods: List<String> = emptyList(),

    @SerializedName("region")
    val region: String = "US",

    @SerializedName("language")
    val language: String = "en"
)