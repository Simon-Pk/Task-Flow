package com.example.taskflow.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusSources @Inject constructor() {
    private val statusRef = FirebaseDatabase.getInstance().getReference("Statuses")

    fun statusSource(): DatabaseReference {
        return statusRef
    }
}
