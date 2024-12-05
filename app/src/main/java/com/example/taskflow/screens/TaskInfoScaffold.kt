package com.example.taskflow.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.data.Comment
import com.example.taskflow.data.TaskModel
import com.example.taskflow.viewmodels.TaskViewModel
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.TopAppBar
import com.ravenzip.workshop.data.TextConfig
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoScaffold(
    padding: PaddingValues,
    taskInfo: MutableState<TaskModel>,
    taskViewModel: TaskViewModel = hiltViewModel<TaskViewModel>(),
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val commentContent = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Главный экран", backArrow = null, items = listOf()) },
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet.value = true },
            ) {
                Icon(Icons.Filled.Comment, contentDescription = "Добавить")
            }
        }
    ) { innerPadding ->
        TaskInfo(padding = innerPadding, taskInfo = taskInfo, taskViewModel = taskViewModel)
        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { CloseModalSheet(showBottomSheet, commentContent) },
                sheetState = rememberModalBottomSheetState(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))

                    MultilineTextField(text = commentContent, label = "content")

                    Spacer(modifier = Modifier.height(5.dp))
                    SimpleButton(
                        text = "Создать",
                        textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
                    ) {
                        scope.launch {
                            taskViewModel.createComment(
                                Comment(
                                    "",
                                    commentContent.value,
                                    taskViewModel.currentUser?.uid.toString()
                                )
                            )
                            CloseModalSheet(showBottomSheet, commentContent)
                        }
                    }
                }
            }
        }
    }
}

fun CloseModalSheet(
    showBottomSheet: MutableState<Boolean>,
    commentContent: MutableState<String>,
) {
    showBottomSheet.value = false
    commentContent.value = ""
}
