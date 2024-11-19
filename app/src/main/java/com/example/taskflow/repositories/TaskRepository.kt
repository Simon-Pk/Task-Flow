package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.sources.TaskSources
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
                emit(response)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getTaskList", "${it.message}") }
                emit(listOf())
            }
}
