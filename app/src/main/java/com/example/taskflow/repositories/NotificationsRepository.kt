package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.Notifications
import com.example.taskflow.sources.NotificationsSources
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class NotificationsRepository
@Inject
constructor(private val notificationsSources: NotificationsSources) {
    fun getNotificationsList(userId: String?) =
        flow {
                val response =
                    notificationsSources
                        .notificationsSource()
                        .orderByChild("userId")
                        .equalTo(userId)
                        .get()
                        .await()
                        .children
                val notificationsList = response.convertToClass<Notifications>()
                emit(notificationsList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getNotificationsList", "${it.message}") }
                emit(listOf())
            }

    suspend fun createNotificationData(notifications: Notifications) {
        val firebase = FirebaseDatabase.getInstance()
        val notificationUID = firebase.reference.push().key.toString()
        notificationsSources
            .notificationsSource()
            .child(notificationUID)
            .setValue(notifications.copy(uid = notificationUID))
            .await()
    }

    suspend fun delete(notificationId: String) {
        notificationsSources.notificationsSource().child(notificationId).removeValue().await()
    }
}
