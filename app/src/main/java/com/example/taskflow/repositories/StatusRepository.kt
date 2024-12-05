package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.Status
import com.example.taskflow.sources.StatusSources
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
class StatusRepository @Inject constructor(private val statusSources: StatusSources) {
    fun getStatusList() =
        flow {
                val response = statusSources.statusSource().get().await().children
                val statusList = response.convertToClass<Status>()
                emit(statusList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getStatusList", "${it.message}") }
                emit(listOf())
            }

    suspend fun createStatusData(status: Status) {
        val firebase = FirebaseDatabase.getInstance()
        //        val statusRef = firebase.getReference("Statuses")
        val statusUID = firebase.reference.push().key.toString()
        statusSources.statusSource().child(statusUID).setValue(status.copy(uid = statusUID)).await()
    }

    suspend fun getStatus(statusUid: String): Iterable<DataSnapshot> {
        return statusSources.statusSource().child(statusUid).get().await().children
    }
}
