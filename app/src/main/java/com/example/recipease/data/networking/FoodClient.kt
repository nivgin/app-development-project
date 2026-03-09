package com.example.recipease.data.networking

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FoodClient {

    @GET("foods/search/v1")
    fun getFoodsFreeLanguage(
        @Query("search_expression") expression: String,
        @Query("format") format: String = "json"
    ): Call<ResponseBody>
}