package com.example.taskflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Priority
import com.example.taskflow.data.Status
import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val priorityRepository: PriorityRepository,
    private val statusRepository: StatusRepository,
) : ViewModel() {

    fun createPriority(priority: Priority) {
        viewModelScope.launch { priorityRepository.createPriorityData(priority) }
    }

    fun createStatus(status: Status) {
        viewModelScope.launch { statusRepository.createStatusData(status) }
    }
}
