package com.example.recipease.model

import android.content.Context
import android.os.Parcelable
import com.example.recipease.base.Identifiable
import com.example.recipease.base.RecipeaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey override var id: String,
    val displayName: String,
    val profilePictureUrl: String?,
    val lastUpdated: Long?
) : Parcelable, Identifiable {

    companion object {

        var lastUpdated: Long
            get() {
                return RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("USER_TAG", Context.MODE_PRIVATE)
                    ?.getLong(LAST_UPDATED_KEY, 0) ?: 0
            }
            set(value) {
                RecipeaseApp.Globals.appContext
                    ?.getSharedPreferences("USER_TAG", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.putLong(LAST_UPDATED_KEY, value)
                    ?.apply()
            }

        const val USER_ID_KEY = "userId"
        const val DISPLAY_NAME_KEY = "displayName"
        const val PROFILE_PICTURE_URL_KEY = "profilePictureUrl"
        const val LAST_UPDATED_KEY = "lastUpdated"

        fun fromJson(json: Map<String, Any?>): User {
            val userId = json[USER_ID_KEY] as String
            val displayName = json[DISPLAY_NAME_KEY] as String
            val profilePictureUrl = json[PROFILE_PICTURE_URL_KEY] as? String

            val timestamp = json[LAST_UPDATED_KEY] as? Timestamp
            val lastUpdatedLong = timestamp?.toDate()?.time

            return User(
                id = userId,
                displayName = displayName,
                profilePictureUrl = profilePictureUrl,
                lastUpdated = lastUpdatedLong
            )
        }
    }

    val toJson: Map<String, Any?>
        get() = hashMapOf(
            USER_ID_KEY to id,
            DISPLAY_NAME_KEY to displayName,
            PROFILE_PICTURE_URL_KEY to profilePictureUrl,
            LAST_UPDATED_KEY to FieldValue.serverTimestamp()
        )
}
