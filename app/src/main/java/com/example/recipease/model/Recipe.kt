package com.example.recipease.model

data class Recipe (
    val name: String,
    val description: String,
    val time: String,
    val difficulty: String,
    var author: String,
    var tags: List<String>,
    var ingredients: List<Ingredient>,
    var steps: List<String>
)