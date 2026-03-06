package com.example.recipease.data.repository

import com.example.recipease.data.networking.NetworkClient
import com.example.recipease.model.FreeLanguageBody
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class tokenRepoTest {

    @Test
    fun testGetValidToken() = runBlocking {
        val token = TokenRepository.shared.getValidToken()
        println("Token: $token")
        assertNotNull("Token should not be null", token)
    }

    @Test
    fun testFreeLanguage() = runBlocking {
        val body = FoodsRepository.shared.getFoodsFreeLanguage("2 eggs, 5 pounds ground beef")
        println("Body: $body")
        assertNotNull("Body should not be null", body)
    }
}