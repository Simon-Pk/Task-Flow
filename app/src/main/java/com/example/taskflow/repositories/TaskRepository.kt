package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.TaskModel
import com.example.taskflow.sources.TaskSources
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
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

    suspend fun getListTasks(executorId: String, statusId: String): List<TaskModel> {
        val response =
            taskSources
                .taskSource()
                .orderByChild("executorId")
                .equalTo(executorId)
                .get()
                .await()
                .children
        val listTasks = mutableListOf<TaskModel>()
        response.forEach { data ->
            val task = data.getValue<TaskModel>()
            if (statusId == "") {
                task?.let { listTasks.add(it) }
            } else {
                if (task?.statusId == statusId) { // Дополнительная фильтрация по statusId
                    task?.let { listTasks.add(it) }
                }
            }
        }
        return listTasks
    }

    suspend fun createTaskData(taskModel: TaskModel) {
        val firebase = FirebaseDatabase.getInstance()
        val taskUID = firebase.reference.push().key.toString()
        taskSources.taskSource().child(taskUID).setValue(taskModel.copy(uid = taskUID)).await()
    }

    suspend fun updateTaskData(taskModel: TaskModel) =
        flow {
                val firebase = FirebaseDatabase.getInstance()
                val taskData =
                    mapOf(
                        "uid" to taskModel.uid,
                        "title" to taskModel.title,
                        "startDate" to taskModel.startDate,
                        "finishDate" to taskModel.finishDate,
                        "priorityId" to taskModel.priorityId,
                        "content" to taskModel.content,
                        "executorId" to taskModel.executorId,
                        "statusId" to taskModel.statusId
                    )
                taskSources.taskSource().child(taskModel.uid).updateChildren(taskData).await()
                Log.d("updateTaskData", taskData.toString())
                emit("success")
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("updateTaskData", "${it.message}") }
                emit("error")
            }
}
