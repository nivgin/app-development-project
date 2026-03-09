package com.example.recipease.data.repository

import android.util.Log
import com.example.recipease.data.networking.NetworkClient
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodsRepository private constructor() {

    companion object {
        val shared = FoodsRepository()
        private const val TAG = "FoodsRepository"
    }

    fun searchFoods(expression: String): String? {
        try {

            val response = NetworkClient.foodsApiClientSignpost.getFoodsFreeLanguage(expression).execute()

            if (response.isSuccessful) {
                return response.body()?.string()
            } else {
                Log.e(TAG, "getFoodsFreeLanguage failed: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "searchFoods error: ${e.message}")
            return null
        }
    }
}

