package com.example.recipease.data.networking

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.recipease.data.repository.TokenRepository

class FoodsAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenRepository.shared.getValidToken()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}

class FoodsAuthGenerationInterceptor : Interceptor {

    companion object {
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = "${CLIENT_ID}:${CLIENT_SECRET}"
        val encoded = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic $encoded")
            .build()

        return chain.proceed(request)
    }
}

object NetworkClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(FoodsAuthInterceptor())
            .build()
    }

    private val okHttpClientAuth: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(FoodsAuthGenerationInterceptor())
            .build()
    }

    val foodsAuthClient: FoodAuthClient by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oauth.fatsecret.com/")
            .client(okHttpClientAuth)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FoodAuthClient::class.java)
    }

    val foodsApiClient: FoodClient by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://platform.fatsecret.com/rest/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FoodClient::class.java)
    }
}