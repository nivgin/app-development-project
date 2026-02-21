package com.example.recipease.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    var amount: String = "",
    var name: String = ""
) : Parcelable
