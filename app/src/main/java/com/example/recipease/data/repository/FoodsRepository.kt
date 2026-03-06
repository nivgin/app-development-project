package com.example.recipease.data.repository

import android.util.Log
import com.example.recipease.data.networking.NetworkClient
import com.example.recipease.model.FoodResponse
import com.example.recipease.model.FreeLanguageBody
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodsRepository private constructor() {

    companion object {
        val shared = FoodsRepository()
        private const val TAG = "FoodsRepository"
    }

    fun getFoodsFreeLanguage(userInput: String): JsonObject? {
        try {

            val body = FreeLanguageBody(userInput = userInput)
            val response = NetworkClient.foodsApiClient.getFoodsFreeLanguage(body).execute()

            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e(TAG, "getFoodsFreeLanguage failed: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getFoodsFreeLanguage error: ${e.message}")
            return null
        }
    }
}

