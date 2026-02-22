package com.example.recipease.base

import android.app.Application
import android.content.Context

class RecipeaseApp: Application() {

    companion object Globals {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}