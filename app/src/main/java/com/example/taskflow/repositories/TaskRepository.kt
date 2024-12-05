package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.TaskModel
import com.example.taskflow.sources.TaskSources
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class TaskRepository @Inject constructor(private val taskSources: TaskSources) {
    fun getTaskList() =
        flow {
                val response = taskSources.taskSource().get().await().children
                val taskList = response.convertToClass<TaskModel>()
                emit(taskList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getTaskList", "${it.message}") }
                emit(listOf())
            }

    suspend fun createTaskData(taskModel: TaskModel) {
        val firebase = FirebaseDatabase.getInstance()
        //        val taskRef = firebase.getReference("Task")
        val taskUID = firebase.reference.push().key.toString()
        taskSources.taskSource().child(taskUID).setValue(taskModel.copy(uid = taskUID)).await()
    }
}
