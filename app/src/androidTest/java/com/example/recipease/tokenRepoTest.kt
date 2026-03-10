package com.example.recipease.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class tokenRepoTest {

    @Test
    fun testSearch() = runBlocking {
        val body = FoodsRepository.shared.searchFoods("chicken")
        println("Body: $body")
        assertNotNull("Body should not be null", body)
    }

    @Test
    fun testGetById() = runBlocking {
        val body = FoodsRepository.shared.getFoodById("1641")
        println("Body: $body")
        assertNotNull("Body should not be null", body)
    }
}