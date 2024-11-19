package com.example.taskflow.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Priority
import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.TaskRepository
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.selection.SelectableChipConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel
@Inject
constructor(
    private val taskRepository: TaskRepository,
    private val priorityRepository: PriorityRepository
) : ViewModel() {
    private val _priorityList = MutableStateFlow(listOf(Priority()))
    val priorityList = _priorityList.asStateFlow()

    val prioritiesChips =
        _priorityList
            .map { priorityList -> generateListOfPriorities(priorityList) }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableStateListOf())

    init {
        viewModelScope.launch {
            taskRepository.getTaskList().collect {}
            priorityRepository.getPriorityList().collect { priorityList ->
                _priorityList.update { priorityList }
            }
        }
    }

    fun generateListOfPriorities(
        listOfPriority: List<Priority>
    ): SnapshotStateList<SelectableChipConfig> {
        return listOfPriority
            .sortedBy { priority: Priority -> priority.id }
            .mapIndexed { index, priority ->
                SelectableChipConfig(
                    isSelected = index == 0,
                    text = priority.name,
                    textConfig = TextConfig.Small,
                    icon = Icon.ImageVectorIcon(Icons.Filled.LowPriority),
                    iconConfig = IconConfig.Small
                )
            }
            .toMutableStateList()
    }
}
