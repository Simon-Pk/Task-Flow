package com.example.taskflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Comment
import com.example.taskflow.data.DataComment
import com.example.taskflow.data.Priority
import com.example.taskflow.data.Status
import com.example.taskflow.data.User
import com.example.taskflow.repositories.CommentRepository
import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.StatusRepository
import com.example.taskflow.repositories.TaskRepository
import com.example.taskflow.repositories.UserRepository
import com.ravenzip.kotlinflowextended.functions.forkJoin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TaskInfoViewModel
@Inject
constructor(
    private val taskRepository: TaskRepository,
    private val priorityRepository: PriorityRepository,
    private val userRepository: UserRepository,
    private val statusRepository: StatusRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    private val _activeTaskId = MutableStateFlow("")
    val activeTaskId = _activeTaskId.asStateFlow()
    val _commentList = MutableStateFlow(listOf(DataComment()))
    val commentList = _commentList.asStateFlow()
    val currentUser = userRepository.currentUser

    init {
        viewModelScope.launch { updateListComments() }
    }

    fun getUserAndMapToDataComment(comment: Comment) = flow {
        val userById = getUser(comment.userId)
        emit(DataComment(comment.uid, comment.content, comment.taskId, userById))
    }

    suspend fun getPriority(priorityUid: String): Priority? {
        return priorityRepository.getPriority(priorityUid)
    }

    suspend fun getStatus(statusUid: String): Status? {
        return statusRepository.getStatus(statusUid)
    }

    suspend fun createComment(comment: Comment) {
        commentRepository.createCommentsData(comment)
    }

    suspend fun getUser(userUid: String): User {
        return userRepository.getUserById(userUid)
    }

    fun updateTaskId(taskId: String) {
        _activeTaskId.update { taskId }
    }

    suspend fun updateListComments() {
        activeTaskId
            .flatMapLatest { taskId ->
                commentRepository
                    .getCommentsList(taskId)
                    .filter { it.isNotEmpty() }
                    .flatMapLatest { comments ->
                        forkJoin(comments.map { comment -> getUserAndMapToDataComment(comment) })
                    }
            }
            .take(1)
            .stateIn(viewModelScope, SharingStarted.Lazily, listOf())
            .collect { commentList -> _commentList.update { commentList } }
    }
}
