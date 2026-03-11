package com.example.recipease.data.networking

import android.R
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import com.example.recipease.model.FoodIdSearchResponse
import com.example.recipease.model.FoodsSearchResponse

interface FoodClient {

    @GET("foods/search/v1")
    fun searchFoods(
        @Query("search_expression") expression: String,
        @Query("format") format: String = "json",
        @Query("max_results") maxResults: Int = 10
    ): Call<FoodsSearchResponse>

    @GET("food/v5")
    fun getFoodById(
        @Query("food_id") foodId: String,
        @Query("format") format: String = "json"
    ): Call<FoodIdSearchResponse>
}