package com.example.recipease.data.networking

import android.net.Uri
import android.util.Base64
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object NetworkClient {

    private const val CONSUMER_KEY = "b0f0861ed7f745aeba4bdd7e24bdf2f3"
    private const val CONSUMER_SECRET = "21698085c0fa41df84195f8cf35d9fe9"

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
