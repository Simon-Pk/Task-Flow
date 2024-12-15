package com.example.taskflow.screens.TaskInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.Tools.convertLongToDate
import com.example.taskflow.data.Notifications
import com.example.taskflow.data.Priority
import com.example.taskflow.data.TaskModel
import com.example.taskflow.data.User
import com.example.taskflow.screens.Tasks.Tasks
import com.example.taskflow.viewmodels.TaskViewModel
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.TextConfig
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScaffold(
    padding: PaddingValues,
    taskViewModel: TaskViewModel = hiltViewModel<TaskViewModel>(),
    vararg onClick: () -> Unit,
    taskModel: MutableState<TaskModel>
) {
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val taskName = remember { mutableStateOf("") }
    val taskContent = remember { mutableStateOf("") }

    val priorityList = taskViewModel.priorityList.collectAsStateWithLifecycle().value
    val userList = taskViewModel.userList.collectAsStateWithLifecycle().value
    val statusList = taskViewModel.statusList.collectAsStateWithLifecycle().value
    val taskDateformat = SimpleDateFormat("dd.M.yyyy")
    val currentDate = taskDateformat.format(Date())
    val taskStartDate = remember { mutableStateOf(currentDate) }
    val taskFinishDate = remember { mutableStateOf("") }
    val dropDownFieldExecutorState = remember { mutableStateOf<User>(User()) }
    val dropDownFieldPriorityState = remember { mutableStateOf<Priority>(Priority()) }
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.padding(padding),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet.value = true },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
    ) { innerPadding ->
        Tasks(
            padding = innerPadding,
            onClick = onClick,
            taskModel = taskModel,
            taskViewModel = taskViewModel
        )
        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    CloseModalSheetList(showBottomSheet, taskName, taskContent, taskFinishDate)
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxHeight(0.7f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SinglenessOutlinedTextField(text = taskName, label = "name")

                    Spacer(modifier = Modifier.height(5.dp))

                    DropDownTextField(
                        dropDownFieldPriorityState,
                        priorityList,
                        { it.name },
                        label = "priority"
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Spacer(modifier = Modifier.height(5.dp))

                    MultilineTextField(text = taskContent, label = "content")

                    Spacer(modifier = Modifier.height(5.dp))

                    SinglenessOutlinedTextField(
                        text = taskStartDate,
                        label = "start date",
                        readOnly = true
                    )

                    SinglenessOutlinedTextField(
                        text = taskFinishDate,
                        label = "finish date",
                        readOnly = true,
                        modifier =
                            Modifier.onFocusChanged {
                                if (it.isFocused) {
                                    openDialog.value = true
                                }
                            }
                    )

                    if (openDialog.value) {
                        val datePickerState = rememberDatePickerState()
                        val confirmEnabled = derivedStateOf {
                            datePickerState.selectedDateMillis != null
                        }

                        DatePickerDialog(
                            onDismissRequest = { openDialog.value = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false

                                        var date = "No Selection"
                                        if (datePickerState.selectedDateMillis != null) {
                                            date =
                                                convertLongToDate(
                                                    datePickerState.selectedDateMillis!!
                                                )
                                        }
                                        taskFinishDate.value = date
                                    },
                                    enabled = confirmEnabled.value
                                ) {
                                    Text(text = "Ok")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    DropDownTextField(
                        dropDownFieldExecutorState,
                        userList,
                        { it.name },
                        label = "Исполнитель"
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Spacer(modifier = Modifier.height(5.dp))
                    SimpleButton(
                        text = "Add",
                        textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
                    ) {
                        scope.launch {
                            taskViewModel.createTask(
                                TaskModel(
                                    "",
                                    taskName.value,
                                    statusList.get(0).uid,
                                    taskContent.value,
                                    dropDownFieldExecutorState.value.uid,
                                    taskFinishDate.value,
                                    dropDownFieldPriorityState.value.uid,
                                    taskStartDate.value
                                )
                            )
                            taskViewModel.createNotification(
                                Notifications(
                                    "",
                                    "Добавлена задача: ${taskName.value}",
                                    dropDownFieldExecutorState.value.uid,
                                    currentDate
                                )
                            )
                            CloseModalSheetList(
                                showBottomSheet,
                                taskName,
                                taskContent,
                                taskFinishDate
                            )
                            taskViewModel.updateActiveTab(statusList.get(0).uid)
                        }
                    }
                }
            }
        }
    }
}

fun CloseModalSheetList(
    showBottomSheet: MutableState<Boolean>,
    taskName: MutableState<String>,
    taskContent: MutableState<String>,
    taskFinishDate: MutableState<String>,
) {
    showBottomSheet.value = false
    taskName.value = ""
    taskContent.value = ""
    taskFinishDate.value = ""
}
