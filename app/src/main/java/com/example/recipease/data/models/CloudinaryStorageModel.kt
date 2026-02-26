package com.example.recipease.data.models

import android.content.Context
import android.graphics.Bitmap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.policy.UploadPolicy
import com.example.recipease.base.RecipeaseApp
import com.example.recipease.base.Constants
import com.example.recipease.BuildConfig
import com.example.recipease.base.Identifiable
import java.io.File

class CloudinaryStorageModel {

    fun uploadImage(
        model: Identifiable,
        image: Bitmap,
        completion: (String?) -> Unit
    ) {
        val context = RecipeaseApp.appContext ?: return
        val file = bitmapToFile(image, context)

        val folderName = Constants.folderFor(model)

        MediaManager.get().upload(file.path)
            .option("folder", "$folderName/${model.id}")
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
