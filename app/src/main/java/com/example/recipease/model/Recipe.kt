package com.example.recipease.model

data class Recipe (
    val name: String,
    val description: String,
    val time: String,
    val difficulty: String,
    var author: String,
    var tags: List<String>
)