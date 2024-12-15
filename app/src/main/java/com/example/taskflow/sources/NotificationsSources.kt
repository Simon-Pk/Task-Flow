package com.example.taskflow.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsSources @Inject constructor() {
    private val notificationRef = FirebaseDatabase.getInstance().getReference("Notifications")

    fun notificationsSource(): DatabaseReference {
        return notificationRef
    }
}
