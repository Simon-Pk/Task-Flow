package com.example.taskflow.services.firebase

import android.util.Log
import com.example.taskflow.data.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class UserService() {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    private val _dataUser = MutableStateFlow(User())
    val dataUser = _dataUser.asStateFlow()

    suspend fun add(currentUser: FirebaseUser?) {
        if (currentUser !== null) {
            databaseRef.child(currentUser.uid).setValue(User(currentUser.uid, "")).await()
        } else {
            Log.d("Exception", "currentUser is null")
        }
    }

    suspend fun get(currentUser: FirebaseUser?) {
        if (currentUser !== null) {
            val response = databaseRef.child(currentUser.uid).get().await()
            val data = response.getValue<User>()
            if (data != null) {
                _dataUser.value = data
            }
            Log.d("UserData", dataUser.value.toString())
        } else {
            Log.d("Exception", "currentUser is null")
        }
    }

    suspend fun update(userData: User, name: String): Boolean {
        if (userData !== null) {
            val data = mapOf("name" to name)
            databaseRef.child(userData.id).updateChildren(data)
            return true
        } else {
            Log.d("Exception", "dataUser is null")
            return false
        }
    }
}
