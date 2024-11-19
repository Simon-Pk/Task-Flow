package com.example.taskflow.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskSources @Inject constructor() {
    private val taskRef = FirebaseDatabase.getInstance().getReference("Task")

    fun taskSource(): DatabaseReference {
        return taskRef
    }
}
