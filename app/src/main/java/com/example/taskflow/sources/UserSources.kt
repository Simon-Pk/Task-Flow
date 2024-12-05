package com.example.taskflow.sources

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSources @Inject constructor() {
    private val userRef = FirebaseDatabase.getInstance().getReference("Users")
    private val firebaseInstance = FirebaseAuth.getInstance()

    fun userSource(): DatabaseReference {
        return userRef
    }

    val currentUser: FirebaseUser?
        get() = firebaseInstance.currentUser
}
