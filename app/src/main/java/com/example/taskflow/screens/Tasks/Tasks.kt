package com.example.taskflow.screens.Tasks

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.data.Status
import com.example.taskflow.data.TaskModel
import com.example.taskflow.viewmodels.TaskViewModel
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tasks(
    padding: PaddingValues,
    taskViewModel: TaskViewModel,
    vararg onClick: () -> Unit,
    taskModel: MutableState<TaskModel>
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    val statusList = taskViewModel.statusList.collectAsStateWithLifecycle().value
    val currentTabTasks = taskViewModel.currentTabTasks.collectAsStateWithLifecycle().value

    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                taskViewModel.updateListTask(taskViewModel.currentUser?.uid.toString(), "")
                delay(1.seconds)
                isRefreshing.value = false
            }
        },
        modifier = Modifier.padding(padding),
        state = refreshState,
    ) {
        LaunchedEffect(Unit) {
            scope.launch {
                taskViewModel.updateListTask(taskViewModel.currentUser?.uid.toString(), "")
            }
        }
        if (currentTabTasks.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                Column {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.wrapContentWidth(),
                        edgePadding = 16.dp
                    ) {
                        statusList.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title.name) },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(index)

                                        taskViewModel.updateActiveTab(title.uid)
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Список задач пуст")
                    }
                }
            }
        } else {

            Box(modifier = Modifier.padding(padding)) {
                Column {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.wrapContentWidth(),
                        edgePadding = 16.dp
                    ) {
                        statusList.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title.name) },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(index)
                                        taskViewModel.updateActiveTab(title.uid)
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    HorizontalPager(state = pagerState) { pageIndex ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(currentTabTasks) { task ->
                                TaskCard(
                                    taskItem = task,
                                    pagerState = pagerState,
                                    taskViewModel = taskViewModel,
                                    changeCurrentPage = { newPageIndex ->
                                        scope.launch {
                                            pagerState.animateScrollToPage(newPageIndex)
                                        }
                                    },
                                    onClick = {
                                        //
                                        // taskViewModel.updateTaskId(task.uid)
                                        taskModel.value = task
                                        onClick[0]()
                                    },
                                    statusList
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    taskItem: TaskModel,
    pagerState: PagerState,
    taskViewModel: TaskViewModel,
    changeCurrentPage: (Int) -> Unit,
    onClick: () -> Unit,
    statusList: List<Status>
) {
    val offsetX = remember { Animatable(0f) } // Анимация для горизонтального смещения
    val scope = rememberCoroutineScope()
    Card(
        modifier =
            Modifier.fillMaxWidth()
                .padding(10.dp)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val currentPage = pagerState.currentPage
                                val pageCount = pagerState.pageCount
                                var newPageIndex = currentPage
                                when {
                                    // Перемещение на следующий таб
                                    offsetX.value > 300f && currentPage < pageCount - 1 -> {
                                        newPageIndex = currentPage + 1
                                        changeCurrentPage(newPageIndex)
                                    }
                                    // Перемещение на предыдущий таб
                                    offsetX.value < -300f && currentPage > 0 -> {
                                        newPageIndex = currentPage - 1
                                        changeCurrentPage(newPageIndex)
                                    }
                                    else -> {
                                        // Возврат карточки на место
                                        offsetX.animateTo(0f)
                                    }
                                }
                                updateTaskStatus(taskViewModel, taskItem, newPageIndex)
                                taskViewModel.updateActiveTab(statusList.get(newPageIndex).uid)
                                //                                taskViewModel.updateListTask(
                                //
                                // taskViewModel.currentUser?.uid.toString(),
                                //                                    ""
                                //                                )
                            }
                        },
                        onDragCancel = {
                            // Возврат карточки на место при отмене перетаскивания
                            scope.launch { offsetX.animateTo(0f) }
                        }
                    ) { change, dragAmount ->
                        scope.launch {
                            change.consume()
                            // Обновляем смещение во время перетаскивания
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                        }
                    }
                }
                .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Верхняя часть карточки: название задачи и статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Можно заменить на другую иконку
                    contentDescription = "Task Status",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = taskItem.title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Описание задачи
            Text(
                text = taskItem.content,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                maxLines = 3,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

suspend fun updateTaskStatus(taskViewModel: TaskViewModel, taskItem: TaskModel, newPageIndex: Int) {
    val newStatusId = taskViewModel.statusList.value.getOrNull(newPageIndex)?.uid
    if (newStatusId != null && newStatusId != taskItem.statusId) {
        taskViewModel.activeTaskUpdated.emit(taskItem.copy(statusId = newStatusId))
        Log.d("TaskCard", "Task moved to status: $newStatusId")
    }
}
