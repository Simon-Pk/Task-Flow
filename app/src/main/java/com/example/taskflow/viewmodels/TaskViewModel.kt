package com.example.taskflow.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Notifications
import com.example.taskflow.data.Priority
import com.example.taskflow.data.Status
import com.example.taskflow.data.TaskModel
import com.example.taskflow.data.User
import com.example.taskflow.repositories.NotificationsRepository
import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.StatusRepository
import com.example.taskflow.repositories.TaskRepository
import com.example.taskflow.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel
@Inject
constructor(
    private val taskRepository: TaskRepository,
    private val priorityRepository: PriorityRepository,
    private val userRepository: UserRepository,
    private val statusRepository: StatusRepository,
    private val notificationsRepository: NotificationsRepository,
) : ViewModel() {

    private val _priorityList = MutableStateFlow(listOf(Priority()))
    val priorityList = _priorityList.asStateFlow()

    private val _userList = MutableStateFlow(listOf(User()))
    val userList = _userList.asStateFlow()
    val currentUser = userRepository.currentUser

    private val _taskList = MutableStateFlow(listOf<TaskModel>())
    val taskList = _taskList.asStateFlow()

    val _activeTabChanged = MutableStateFlow<String>("")
    val activeTabChanged = _activeTabChanged.asStateFlow()

    val activeTaskUpdated = MutableSharedFlow<TaskModel>()

    val currentTabTasks =
        taskList
            .combine(activeTabChanged) { taskList, activeTab ->
                Log.d("currentTabTasks", activeTab)
                taskList.filter { task ->
                    task.statusId == activeTab && task.executorId == userRepository.currentUser?.uid
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, listOf())
    private val _statusList = MutableStateFlow(listOf(Status()))
    val statusList = _statusList.asStateFlow()

    init {
        Log.d("start viewmodel", "123")
        viewModelScope.launch {
            priorityRepository.getPriorityList().collect { priorityList ->
                _priorityList.update { priorityList }
            }
        }

        viewModelScope.launch {
            userRepository.getUserList().collect { userList -> _userList.update { userList } }
        }

        viewModelScope.launch {
            statusRepository
                .getStatusList()
                .zip(taskRepository.getTaskList()) { statusList, taskList ->
                    _statusList.update { statusList }
                    _taskList.update { taskList }
                }
                .collect { _activeTabChanged.update { statusList.value.first().uid } }
        }

        viewModelScope.launch {
            activeTaskUpdated
                .flatMapLatest { task -> taskRepository.updateTaskData(task) }
                .combine(currentTabTasks) { task, tabTask -> currentTabTasks.collect { tabTask } }
                .collect {}
        }

        viewModelScope.launch {
            activeTabChanged.collect { activeTab ->
                _activeTabChanged.update { activeTab }
                Log.d("пиздапиздапизда", activeTab)
            }
        }

        viewModelScope.launch {
            activeTaskUpdated.collect { Log.d("activeTaskUpdated", it.toString()) }
        }
    }

    suspend fun createTask(taskModel: TaskModel) {
        taskRepository.createTaskData(taskModel)
    }

    fun createPriority(priority: Priority) {
        viewModelScope.launch { priorityRepository.createPriorityData(priority) }
    }

    suspend fun createNotification(notification: Notifications) {
        notificationsRepository.createNotificationData(notification)
    }

    fun updateActiveTab(tabUid: String) {
        _activeTabChanged.update { tabUid }
        viewModelScope.launch {
            updateListTask(currentUser?.uid.toString(), activeTabChanged.value)
        }
    }

    suspend fun updateListTask(executorId: String, statusId: String) {
        _taskList.update { taskRepository.getListTasks(executorId, statusId) }
    }
}
