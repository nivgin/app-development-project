package com.example.recipease.model

import android.content.Context
import com.example.recipease.base.RecipeaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class Tags(
    val tags: List<String>,
    val lastUpdated: Long?
) {
    companion object {

        var lastUpdated: Long
            get() {
                return RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("TAGS", Context.MODE_PRIVATE)
                    ?.getLong(LAST_UPDATED_KEY, 0) ?: 0
            }
            set(value) {
                RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("TAGS", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.putLong(LAST_UPDATED_KEY, value)
                    ?.apply()
            }

        const val TAGS_KEY = "tags"
        const val LAST_UPDATED_KEY = "lastUpdated"

        fun fromJson(json: Map<String, Any?>): Tags {
            val tags = json[TAGS_KEY] as? List<String> ?: emptyList()
            val timestamp = json[LAST_UPDATED_KEY] as? Timestamp
            val lastUpdatedLong = timestamp?.toDate()?.time

            return Tags(
                tags = tags,
                lastUpdated = lastUpdatedLong
            )
        }
    }

    val toJson: Map<String, Any?>
        get() = hashMapOf(
            TAGS_KEY to tags,
            LAST_UPDATED_KEY to FieldValue.serverTimestamp()
        )
}
