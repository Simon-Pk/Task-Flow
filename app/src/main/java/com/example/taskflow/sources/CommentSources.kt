package com.example.taskflow.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentSources @Inject constructor() {
    private val commentsRef = FirebaseDatabase.getInstance().getReference("Comments")

    fun commentsSource(): DatabaseReference {
        return commentsRef
    }
}
