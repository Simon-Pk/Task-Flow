package com.example.taskflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Comment
import com.example.taskflow.data.Priority
import com.example.taskflow.data.Status
import com.example.taskflow.data.TaskModel
import com.example.taskflow.data.User
import com.example.taskflow.repositories.CommentRepository
import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.StatusRepository
import com.example.taskflow.repositories.TaskRepository
import com.example.taskflow.repositories.UserRepository
import com.google.firebase.database.DataSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val _priorityList = MutableStateFlow(listOf(Priority()))
    val priorityList = _priorityList.asStateFlow()

    private val _commentList = MutableStateFlow(listOf(Comment()))
    val commentList = _commentList.asStateFlow()

    private val _userList = MutableStateFlow(listOf(User()))
    val userList = _userList.asStateFlow()
    val currentUser = userRepository.currentUser

    private val _taskList = MutableStateFlow(listOf(TaskModel()))
    val taskList = _taskList.asStateFlow()
    val activeTabChanged = MutableSharedFlow<String>()
    val currentTabTasks =
        _taskList
            .combine(activeTabChanged) { taskList, activeTab ->
                taskList.filter { task ->
                    task.status == activeTab && task.executor == userRepository.currentUser?.uid
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    private val _statusList = MutableStateFlow(listOf(Status()))
    val statusList = _statusList.asStateFlow()

    //    val prioritiesChips =
    //        _priorityList
    //            .map { priorityList -> generateListOfPriorities(priorityList) }
    //            .stateIn(viewModelScope, SharingStarted.Lazily, mutableStateListOf())

    //    val selectedPriority =
    //        prioritiesChips
    //            .map { prioritiesChips -> prioritiesChips.first { chip -> chip.isSelected } }
    //            .zip(_priorityList) { chip, priorityList ->
    //                priorityList.first { priority -> priority.name == chip.text }
    //            }
    //            .stateIn(viewModelScope, SharingStarted.Lazily, Priority())

    init {
        viewModelScope.launch {
            statusRepository
                .getStatusList()
                .zip(taskRepository.getTaskList()) { statusList, taskList ->
                    _statusList.update { statusList }
                    _taskList.update { taskList }
                }
                .collect { activeTabChanged.emit("-OD8FWvgqOCsN5XdTcha") }

            priorityRepository.getPriorityList().collect { priorityList ->
                _priorityList.update { priorityList }
            }

            commentRepository.getCommentsList().collect { commentList ->
                _commentList.update { commentList }
            }

            userRepository.getUserList().collect { userList -> _userList.update { userList } }
        }
    }

    //    private fun generateListOfPriorities(
    //        listOfPriority: List<Priority>
    //    ): SnapshotStateList<SelectableChipConfig> {
    //        return listOfPriority
    //            .sortedBy { priority: Priority -> priority.code }
    //            .mapIndexed { index, priority ->
    //                SelectableChipConfig(
    //                    isSelected = index == 0,
    //                    text = priority.name,
    //                    textConfig = TextConfig.Small,
    //                    icon = Icon.ImageVectorIcon(Icons.Filled.LowPriority),
    //                    iconConfig = IconConfig.Small
    //                )
    //            }
    //            .toMutableStateList()
    //    }

    suspend fun createTask(taskModel: TaskModel) {
        taskRepository.createTaskData(taskModel)
    }

    fun createPriority(priority: Priority) {
        viewModelScope.launch { priorityRepository.createPriorityData(priority) }
    }

    suspend fun getPriority(priorityUid: String): Iterable<DataSnapshot> {
        return priorityRepository.getPriority(priorityUid)
    }

    suspend fun getStatus(statusUid: String): Iterable<DataSnapshot> {
        return statusRepository.getStatus(statusUid)
    }

    suspend fun createComment(comment: Comment) {
        commentRepository.createCommentsData(comment)
    }
}
