package com.example.recipease.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipease.dao.AppLocalDB
import com.example.recipease.dao.AppLocalDbRepository
import com.example.recipease.data.models.FirebaseModel
import com.example.recipease.data.models.CloudinaryStorageModel
import com.example.recipease.model.User
import java.util.concurrent.Executors

class UserRepository private constructor() {

    private val firebaseModel = FirebaseModel()
    private val storageModel = CloudinaryStorageModel()
    private val executor = Executors.newSingleThreadExecutor()

    private val database: AppLocalDbRepository = AppLocalDB.db

    var connectedUser: LiveData<User?> = MutableLiveData(null)

    companion object {
        val shared = UserRepository()
    }

    fun refreshUsers(completion: () -> Unit = {}) {
        firebaseModel.getAllUsers(User.lastUpdated) { fetchedUsers ->
            executor.execute {
                var time = User.lastUpdated
                for (user in fetchedUsers) {
                    database.userDao.insertUsers(user)
                    user.lastUpdated?.let { userLastUpdated ->
                        if (time < userLastUpdated) {
                            time = userLastUpdated
                        }
                    }
                }
                User.lastUpdated = time
            }
            completion()
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        return database.userDao.getAllUsers()
    }

    fun getUserById(id: String): LiveData<User?> {
        return database.userDao.getUserById(id)
    }

    fun addUser(user: User, userImage: Bitmap, completion: () -> Unit) {
        firebaseModel.addUser(user) {
            storageModel.uploadImage(user, userImage) { pictureUrl ->
                if (!pictureUrl.isNullOrEmpty()) {
                    val userCopy = user.copy(profilePictureUrl = pictureUrl)
                    firebaseModel.addUser(userCopy, completion)
                } else {
                    completion()
                }
            }
        }
    }
}
