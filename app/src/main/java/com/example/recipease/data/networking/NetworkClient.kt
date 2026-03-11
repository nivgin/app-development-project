package com.example.recipease.data.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import com.example.recipease.BuildConfig

object NetworkClient {

    private const val CONSUMER_KEY = BuildConfig.FATSECRET_API_KEY
    private const val CONSUMER_SECRET = BuildConfig.FATSECRET_API_SECRET

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val consumer = OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET)
                val signed = consumer.sign(chain.request()).unwrap() as okhttp3.Request
                chain.proceed(signed)
            }
            .build()
    }

    val foodsApiClientSignpost: FoodClient by lazy {
        Retrofit.Builder()
            .baseUrl("https://platform.fatsecret.com/rest/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodClient::class.java)
    }
}
