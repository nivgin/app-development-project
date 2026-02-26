package com.example.recipease.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipease.data.models.FirebaseModel
import com.example.recipease.data.models.CloudinaryStorageModel
import com.example.recipease.model.User
import java.util.concurrent.Executors

class UserRepository private constructor() {

    private val firebaseModel = FirebaseModel()
    private val storageModel = CloudinaryStorageModel()
    private val executor = Executors.newSingleThreadExecutor()
    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> get() = _users

    private val _connectedUser = MutableLiveData<User?>()
    val connectedUserLive: LiveData<User?> get() = _connectedUser

    var connectedUser: User?
        get() = _connectedUser.value
        set(value) {
            _connectedUser.postValue(value)
        }

    companion object {
        val shared = UserRepository()
    }

    fun getAllUsers() {
        firebaseModel.getAllUsers(User.lastUpdated) { fetchedUsers ->
            executor.execute {
                var time = User.lastUpdated
                for (user in fetchedUsers) {
                    user.lastUpdated?.let { userLastUpdated ->
                        if (time < userLastUpdated) {
                            time = userLastUpdated
                        }
                    }
                }
                User.lastUpdated = time

                _users.postValue(fetchedUsers)
            }
        }
    }

    fun addUser(user: User, userImage: Bitmap, completion: () -> Unit) {
        firebaseModel.addUser(user) {
            storageModel.uploadImage(user, userImage) {
                    pictureUrl ->
                if (!pictureUrl.isNullOrEmpty()) {
                    val userCopy = user.copy(profilePictureUrl = pictureUrl)
                    firebaseModel.addUser(userCopy, completion)
                } else {
                    completion()
                }
            }
        }
    }

    fun getUserById(userId: String, completion: (User?) -> Unit) {
        // ADD DAO TO THIS, IT CURRENTLY CALLS THE DB, ONCE WE HAVE A DAO WITH ROOM WE CAN CALL THE LOCALDB WITH ALL USERS BEFORE, AND IT HSOULD BE FINE.
        firebaseModel.getUserById(userId) { user -> completion(user) }
    }
}
