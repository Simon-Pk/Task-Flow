package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.Priority
import com.example.taskflow.sources.PrioritySources
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class PriorityRepository @Inject constructor(private val prioritySources: PrioritySources) {
    fun getPriorityList() =
        flow {
                val response = prioritySources.prioritySource().get().await().children
                val priorityList = response.convertToClass<Priority>()
                emit(priorityList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getPriorityList", "${it.message}") }
                emit(listOf())
            }

    suspend fun createPriorityData(priority: Priority) {
        val firebase = FirebaseDatabase.getInstance()
        //        val priorityRef = firebase.getReference("Priorities")
        val priorityUID = firebase.reference.push().key.toString()
        prioritySources
            .prioritySource()
            .child(priorityUID)
            .setValue(priority.copy(uid = priorityUID))
            .await()
    }

    suspend fun getPriority(priorityUid: String): Iterable<DataSnapshot> {
        return prioritySources.prioritySource().child(priorityUid).get().await().children
    }
}
