package com.example.recipease.data.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import com.example.recipease.BuildConfig

import oauth.signpost.http.HttpParameters

object NetworkClient {

    private const val CONSUMER_KEY = BuildConfig.FATSECRET_API_KEY
    private const val CONSUMER_SECRET = BuildConfig.FATSECRET_API_SECRET

    private fun getServerTimestamp(): String {
        return try {
            val connection = java.net.URL("https://platform.fatsecret.com/rest/server.api")
                .openConnection() as java.net.HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.connect()
            val dateHeader = connection.getHeaderField("Date")
            connection.disconnect()
            if (dateHeader != null) {
                val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US)
                val serverTime = sdf.parse(dateHeader)?.time ?: System.currentTimeMillis()
                (serverTime / 1000).toString()
            } else {
                (System.currentTimeMillis() / 1000).toString()
            }
        } catch (e: Exception) {
            (System.currentTimeMillis() / 1000).toString()
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val consumer = OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET)

                // Inject server-synced timestamp directly into OAuth params
                val params = HttpParameters()
                params.put("oauth_timestamp", getServerTimestamp())
                consumer.setAdditionalParameters(params)

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
