package com.example.recipease.data.networking

import com.example.recipease.model.FoodResponse
import com.example.recipease.model.FreeLanguageBody
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface FoodClient {

    @POST("natural-language-processing/v1")
    fun getFoodsFreeLanguage(
        @Body request: FreeLanguageBody
    ): Call<JsonObject>
}