package com.example.taskflow.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.TaskModel
import com.example.taskflow.repositories.SharedRepository
import com.example.taskflow.repositories.StatusRepository
import com.example.taskflow.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val taskRepository: TaskRepository,
    private val statusRepository: StatusRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    private val _taskList = MutableStateFlow(listOf<TaskModel>())
    val taskList = _taskList.asStateFlow()
    val currentUser = sharedRepository.currentUser

    init {
        viewModelScope.launch { updateListTask(currentUser?.uid.toString(), "") }
    }

    suspend fun updateListTask(executorId: String, statusId: String) {
        _taskList.update { taskRepository.getListTasks(executorId, statusId) }
    }

    fun groupTasksByStatus(tasks: List<TaskModel>): Map<String, Int> {
        return tasks.groupingBy { it.statusId }.eachCount()
    }

    suspend fun getStatus(tasks: Map<String, Int>): List<String> {
        val data = mutableListOf<String>()
        tasks.keys.forEach { task -> data.add(statusRepository.getStatus(task)?.name.toString()) }
        Log.d("lalala", data.toString())
        return data
    }
}
