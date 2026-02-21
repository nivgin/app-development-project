package com.example.recipease.data.models

import android.content.Context
import android.graphics.Bitmap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.policy.UploadPolicy
import com.example.recipease.base.RecipeaseApp
import com.example.recipease.model.Recipe
import com.example.recipease.BuildConfig
import java.io.File

class CloudinaryStorageModel {

    init {
        val config = mapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
            "api_key" to BuildConfig.CLOUDINARY_API_KEY,
            "api_secret" to BuildConfig.CLOUDINARY_API_SECRET
        )

        RecipeaseApp.appContext?.let {
            MediaManager.init(it, config)
            MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.Builder()
                .maxConcurrentRequests(3)
                .networkPolicy(UploadPolicy.NetworkType.UNMETERED)
                .build()
        }
    }

    fun uploadRecipeImage(recipe: Recipe, image: Bitmap, completion: (String?) -> Unit) {
        val context = RecipeaseApp.appContext ?: return
        val file = bitmapToFile(image, context)

        MediaManager.get().upload(file.path)
            .option("folder", "recipes/${recipe.id}")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    completion(url)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    completion(null)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }

    private fun bitmapToFile(image: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "recipe_${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            image.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file
    }
}
