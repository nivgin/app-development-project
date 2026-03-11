package com.example.recipease.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    var amount: Double = 0.0,
    var food: FoodSearchItem? = null,
    var serving: ModifiedServing? = null,
) : Parcelable
