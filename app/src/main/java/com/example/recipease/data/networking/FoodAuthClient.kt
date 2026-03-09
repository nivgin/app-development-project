package com.example.recipease.data.networking

import com.example.recipease.model.TokenResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface FoodAuthClient {

    @FormUrlEncoded
    @POST("connect/token")
    fun generateToken(
        @Field("grant_type") grantType: String = "client_credentials"
    ): Call<TokenResponse>
}