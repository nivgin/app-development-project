package com.example.recipease.data.repository

import android.util.Log
import com.example.recipease.data.networking.NetworkClient
import com.example.recipease.model.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenRepository private constructor() {

    companion object {
        val shared = TokenRepository()
        private const val TAG = "TokenRepository"
    }

    private var accessToken: String? = null
    private var tokenExpiryTime: Long = 0L

    fun getValidToken(): String? {
        if (isTokenValid()) return accessToken
        return refreshToken()
    }

    private fun isTokenValid(): Boolean {
        return accessToken != null && System.currentTimeMillis() < tokenExpiryTime
    }

    private fun refreshToken(): String? {
        try {
            val response = NetworkClient.foodsAuthClient.generateToken("client_credentials").execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    accessToken = body.accessToken
                    tokenExpiryTime = System.currentTimeMillis() + (body.expiresIn - 60) * 1000L
                    Log.d(TAG, "Token refreshed successfully, expires in ${body.expiresIn}s")
                    return accessToken
                } else {
                    Log.e(TAG, "Token response body was null")
                    return null
                }
            } else {
                Log.e(TAG, "Token request failed: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Token request error: ${e.message}")
            return null
        }
    }
}

