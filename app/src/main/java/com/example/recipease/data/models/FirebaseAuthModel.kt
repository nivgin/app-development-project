package com.example.recipease.data.models

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.User
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthModel {
    private val auth = FirebaseAuth.getInstance()

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    companion object {
        val shared = FirebaseAuthModel()
    }

    fun signIn(email: String, password: String, completion: () -> Unit) {
        if (auth.currentUser != null) { completion(); return }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener  { result ->
                populateUser(result.user?.uid.toString())
                completion()
            }
            .addOnFailureListener {
                completion()
            }
    }

    fun signUp(email: String, password: String, fullname: String, pfp: Bitmap, completion: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val id = result.user?.uid.toString()
                val user = User(id, fullname, "", 0L)
                UserRepository.shared.addUser(user, pfp) {
                    completion()
                }
            }
            .addOnFailureListener {
                completion()
            }
    }

    fun populateUser(id: String) {
        UserRepository.shared.refreshUsers()
        UserRepository.shared.setConnectedUser(UserRepository.shared.getUserById(id))
    }

    fun signOut() {
        auth.signOut()
        UserRepository.shared.setConnectedUser(null)
    }
}