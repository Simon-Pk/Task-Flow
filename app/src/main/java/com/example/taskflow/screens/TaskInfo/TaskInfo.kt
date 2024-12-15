package com.example.taskflow.screens.TaskInfo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.data.TaskModel
import com.example.taskflow.viewmodels.TaskInfoViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfo(
    padding: PaddingValues,
    taskInfoViewModel: TaskInfoViewModel,
    taskInfo: MutableState<TaskModel>
) {
    val taskName = remember { mutableStateOf(taskInfo.value.title) }
    val taskContent = remember { mutableStateOf(taskInfo.value.content) }
    //    val taskExecutor = remember { mutableStateOf(taskInfo.value.executorId) }
    val taskStatus = remember { mutableStateOf("") }
    val taskStartDate = remember { mutableStateOf(taskInfo.value.startDate) }
    val taskFinishDate = remember { mutableStateOf(taskInfo.value.finishDate) }
    //    val taskPriority = remember { mutableStateOf(taskInfo.value.priorityId) }
    val commentsList = taskInfoViewModel.commentList.collectAsStateWithLifecycle(listOf()).value

    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }

    taskInfoViewModel.updateTaskId(taskInfo.value.uid)
    val isLoading = remember { mutableStateOf(true) }
    val taskPriority = remember { mutableStateOf("") }
    LaunchedEffect(isLoading.value) {
        if (isLoading.value) {

            taskPriority.value =
                taskInfoViewModel.getPriority(taskInfo.value.priorityId)?.name.toString()
            taskStatus.value = taskInfoViewModel.getStatus(taskInfo.value.statusId)?.name.toString()

            isLoading.value = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                taskInfoViewModel.updateListComments()
                delay(1.seconds)
                isRefreshing.value = false
            }
        },
        modifier = Modifier.padding(padding),
        state = refreshState,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //        item {
            //            Spacer(modifier = Modifier.height(10.dp))
            //            Text(
            //                text = taskName.value,
            //                modifier = Modifier.padding(start = 10.dp),
            //                fontSize = 30.sp
            //            )
            //        }

            item {
                Spacer(modifier = Modifier.height(60.dp))
                //            Row(
                //                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                //            ) {
                //
                //            }
                SinglenessOutlinedTextField(
                    text = taskStartDate,
                    label = "start date",
                    readOnly = true,
                    //                width = 0.33f,
                    modifier = Modifier.padding(start = 10.dp)
                    //                        .weight(0.5f)
                )
                SinglenessOutlinedTextField(
                    text = taskStartDate,
                    label = "finish date",
                    readOnly = true,
                    //                width = 0.77f,
                    modifier = Modifier.padding(start = 10.dp)
                    //                        .weight(0.5f)
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                SinglenessOutlinedTextField(
                    text = taskStatus,
                    label = "status",
                    readOnly = true,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            //            taskPriority.value =
            //
            // taskInfoViewModel.getPriority(taskInfo.value.priorityId)?.name.toString()

            item {
                Spacer(modifier = Modifier.height(10.dp))
                SinglenessOutlinedTextField(
                    text = taskPriority,
                    label = "priority",
                    readOnly = true,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            //        item {
            //            Spacer(modifier = Modifier.height(10.dp))
            //            SinglenessOutlinedTextField(
            //                text = taskName,
            //                label = "executor",
            //                readOnly = true,
            //                modifier = Modifier.padding(start = 10.dp)
            //            )
            //        }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                SinglenessOutlinedTextField(
                    text = taskContent,
                    label = "content",
                    readOnly = true,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Comments",
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 30.sp
                )
            }
            items(commentsList) { comment ->
                Spacer(modifier = Modifier.height(10.dp))
                InfoCard(
                    icon = Icon.ImageVectorIcon(Icons.Filled.ModeComment),
                    iconConfig = IconConfig.Small,
                    text = comment.content,
                    textConfig = TextConfig.Small,
                    title = comment.userId.name,
                    titleConfig = TextConfig.Small,
                )
            }
        }
    }
}
