package com.example.recipease.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class tokenRepoTest {

    @Test
    fun testFreeLanguage() = runBlocking {
        val body = FoodsRepository.shared.searchFoods("chicken")
        println("Body: $body")
        assertNotNull("Body should not be null", body)
    }
}