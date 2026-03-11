package com.example.recipease.data.repository

import android.util.Log
import com.example.recipease.data.networking.NetworkClient
import com.example.recipease.model.FoodIdSearchResponse
import com.example.recipease.model.FoodsSearchResponse

class FoodsRepository private constructor() {

    companion object {
        val shared = FoodsRepository()
        private const val TAG = "FoodsRepository"
    }

    fun searchFoods(expression: String): FoodsSearchResponse? {
        try {

            val response = NetworkClient.foodsApiClientSignpost.searchFoods(expression).execute()

            if (response.isSuccessful) {
                Log.i(TAG, "searchFoods: success: ${response.code()} ${response.message()} ${response.body()}")
                return response.body()
            } else {
                Log.e(TAG, "searchFoods: failed: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "searchFoods: error: ${e.message}")
            return null
        }
    }

    fun getFoodById(foodId: String): FoodIdSearchResponse? {
        try {

            val response = NetworkClient.foodsApiClientSignpost.getFoodById(foodId).execute()

            if (response.isSuccessful) {
                Log.i(TAG, "getFoodID: success: ${response.code()} ${response.message()} ${response.body()}")
                return response.body()
            } else {
                Log.e(TAG, "getFoodById: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getFoodByIds: error: ${e.message}")
            return null
        }
    }
}

