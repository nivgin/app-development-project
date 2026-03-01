package com.example.recipease.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    val connectedUser: MediatorLiveData<User?> = MediatorLiveData()
    private var currentUserSource: LiveData<User?>? = null

    fun setConnectedUser(source: LiveData<User?>?) {
        currentUserSource?.let { connectedUser.removeSource(it) }
        currentUserSource = source
        if (source != null) {
            connectedUser.addSource(source) { connectedUser.value = it }
        } else {
            connectedUser.value = null
        }
    }

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
