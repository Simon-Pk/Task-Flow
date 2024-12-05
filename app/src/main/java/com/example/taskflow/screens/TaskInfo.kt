package com.example.taskflow.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.data.TaskModel
import com.example.taskflow.viewmodels.TaskViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfo(
    padding: PaddingValues,
    taskViewModel: TaskViewModel,
    taskInfo: MutableState<TaskModel>
) {
    val taskName = remember { mutableStateOf(taskInfo.value.title) }
    val taskContent = remember { mutableStateOf(taskInfo.value.content) }
    val taskExecutor = remember { mutableStateOf(taskInfo.value.executor) }
    val taskStatusId = remember { mutableStateOf(taskInfo.value.status) }
    val taskPriority = remember { mutableStateOf(taskInfo.value.priority) }
    val taskStartDate = remember { mutableStateOf(taskInfo.value.startDate) }
    val taskFinishDate = remember { mutableStateOf(taskInfo.value.finishDate) }

    val commentsList = taskViewModel.commentList.collectAsStateWithLifecycle().value

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = taskName.value,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 30.sp
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            ) {
                SinglenessOutlinedTextField(
                    text = taskStartDate,
                    label = "start date",
                    readOnly = true,
                    width = 0.33f,
                    modifier = Modifier.padding(0.dp)
                )
                SinglenessOutlinedTextField(
                    text = taskStartDate,
                    label = "finish date",
                    readOnly = true,
                    width = 0.77f,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            SinglenessOutlinedTextField(
                text = taskName,
                label = "status",
                readOnly = true,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            SinglenessOutlinedTextField(
                text = taskName,
                label = "priority",
                readOnly = true,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            SinglenessOutlinedTextField(
                text = taskName,
                label = "executor",
                readOnly = true,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

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
            Text(text = "Comments", modifier = Modifier.padding(start = 10.dp), fontSize = 30.sp)
        }
        items(commentsList) { comment ->
            InfoCard(
                icon = Icon.ImageVectorIcon(Icons.Filled.ModeComment),
                iconConfig = IconConfig.Small,
                text = comment.content,
                textConfig = TextConfig.Small,
                title = comment.user,
                titleConfig = TextConfig.Small,
            )
        }
    }
}
