package com.example.taskflow.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskflow.TaskModel

@Composable
fun Tasks(padding: PaddingValues) {
    var state = remember { mutableStateOf(0) }
    val Titles = listOf("Недавно назначенные", "В работе", "Ожидание обратной связи", "Выполненные")
    val ListColors = listOf(Color.Green, Color.Red, Color.Blue, Color.DarkGray)
    Column {
        ScrollableTabRow(
            selectedTabIndex = state.value,
            modifier = Modifier.wrapContentWidth(),
            edgePadding = 16.dp
        ) {
            Titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = state.value == index,
                    onClick = { state.value = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = 1) {
                TaskCard(
                    TaskModel(
                        "Задача ${state.value + it+1}",
                        "Содержание задачи ${state.value + it+1}"
                    )
                )
            }
        }

        //        Text(
        //            modifier = Modifier.align(Alignment.CenterHorizontally),
        //            text = "Задача ${state.value + 1}",
        //            //            style = MaterialTheme.typography.body1
        //        )
        //        LazyRow(modifier = Modifier.fillMaxWidth()) {
        //            items(count = 4) {
        //                Text(
        //                    text = "Колонка ${state.value + it+1}",
        //                    modifier = Modifier.fillParentMaxWidth(1f).background(color =
        // ListColors[it]),
        //                )
        //            }
        //        }
        //
        //        LazyColumn(modifier = Modifier.fillMaxSize()) {
        //            items(count = 100) {
        //                TaskCard(
        //                    TaskModel(
        //                        "Задача ${state.value + it+1}",
        //                        "Содержание задачи ${state.value + it+1}"
        //                    )
        //                )
        //            }
        //        }
    }
}

@Composable
fun TaskCard(TaskItem: TaskModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(10.dp), shape = RoundedCornerShape(15.dp)) {
        Box() {
            Column() {
                Text(modifier = Modifier.padding(5.dp), text = TaskItem.title)
                Spacer(modifier = Modifier.height(15.dp))
                Text(modifier = Modifier.padding(5.dp), text = TaskItem.content)
            }
        }
    }
}
