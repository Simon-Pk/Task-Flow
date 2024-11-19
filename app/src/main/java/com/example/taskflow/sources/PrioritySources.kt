package com.example.taskflow.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrioritySources @Inject constructor() {
    private val priorityRef = FirebaseDatabase.getInstance().getReference("Priorities")

    fun prioritySource(): DatabaseReference {
        return priorityRef
    }
}
