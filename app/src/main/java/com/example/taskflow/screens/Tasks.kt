package com.example.taskflow.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.data.Priority
import com.example.taskflow.data.TaskModel
import com.example.taskflow.data.User
import com.example.taskflow.viewmodels.TaskViewModel
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.TextConfig
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tasks(
    padding: PaddingValues,
    taskViewModel: TaskViewModel = hiltViewModel<TaskViewModel>(),
    vararg onClick: () -> Unit,
    taskModel: MutableState<TaskModel>
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val taskName = remember { mutableStateOf("") }
    val taskContent = remember { mutableStateOf("") }

    val priorityList = taskViewModel.priorityList.collectAsStateWithLifecycle().value
    val userList = taskViewModel.userList.collectAsStateWithLifecycle().value
    val statusList = taskViewModel.statusList.collectAsStateWithLifecycle().value
    val currentTabTasks = taskViewModel.currentTabTasks.collectAsStateWithLifecycle().value
    val taskDateformat = SimpleDateFormat("dd.M.yyyy")
    val currentDate = taskDateformat.format(Date())
    val taskStartDate = remember { mutableStateOf(currentDate) }
    val dropDownFieldExecutorState = remember { mutableStateOf<User>(User()) }
    val dropDownFieldPriorityState = remember { mutableStateOf<Priority>(Priority()) }
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

                                taskViewModel.activeTabChanged.emit(title.uid)
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
                            changeCurrentPage = { newPageIndex ->
                                scope.launch { pagerState.animateScrollToPage(newPageIndex) }
                            },
                            onClick = {
                                taskModel.value = task
                                onClick[0]()
                                //                                taskName.value = task.title
                                //                                taskContent.value = task.content
                                //                                taskStartDate.value =
                                // task.startDate
                                //                                scope.launch {
                                //
                                //                                    //
                                //                                    //
                                // dropDownFieldExecutorState.value = task.executor
                                //
                                //                                    //
                                //                                    //
                                // dropDownFieldPriorityState.value =
                                //                                    //
                                //            taskViewModel
                                //                                    //
                                //                                    // .getPriority(task.priority)
                                //                                    //
                                //                                    // .convertToClass<Priority>()
                                //                                    //
                                //                .get(index = 0)
                                //                                }
                                //                                showBottomSheet.value = true
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { showBottomSheet.value = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 10.dp, bottom = 10.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Добавить")
        }
        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { CloseModalSheetList(showBottomSheet, taskName, taskContent) },
                sheetState = sheetState,
            ) {
                //                val currentPriority =
                //
                // taskViewModel.selectedPriority.collectAsStateWithLifecycle().value

                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SinglenessOutlinedTextField(text = taskName, label = "name")

                    Spacer(modifier = Modifier.height(5.dp))

                    //                    Text("Приоритет:")

                    //                    ChipRadioGroup(list = prioritiesChips)
                    DropDownTextField(
                        dropDownFieldPriorityState,
                        priorityList,
                        { it.name },
                        label = "Приоритет"
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    //                    Text("Сложность:")

                    //                    ChipRadioGroup(list = list) { item ->
                    //                        list.replaceAll { it.copy(isSelected = it.text ==
                    // item.text) }
                    //                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    MultilineTextField(text = taskContent, label = "content")

                    Spacer(modifier = Modifier.height(5.dp))

                    SinglenessOutlinedTextField(
                        text = taskStartDate,
                        label = "Дата начала",
                        readOnly = true
                    )

                    DropDownTextField(
                        dropDownFieldExecutorState,
                        userList,
                        { it.name },
                        label = "Исполнитель"
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Spacer(modifier = Modifier.height(5.dp))
                    SimpleButton(
                        text = "Создать",
                        textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
                    ) {
                        scope.launch {
                            taskViewModel.createTask(
                                TaskModel(
                                    "",
                                    taskName.value,
                                    statusList.get(0).uid,
                                    taskContent.value,
                                    dropDownFieldExecutorState.value.id,
                                    "",
                                    dropDownFieldPriorityState.value.uid,
                                    taskStartDate.value
                                )
                            )
                            CloseModalSheetList(showBottomSheet, taskName, taskContent)
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
    changeCurrentPage: (Int) -> Unit,
    onClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Card(
        modifier =
            Modifier.fillMaxWidth()
                .padding(10.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume() // Учитываем жест

                        // Обновляем смещение для карточки
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y

                        // Логика смены страницы, если карточка выходит за пределы экрана
                        val currentPage = pagerState.currentPage
                        if (offsetX > 300 && currentPage < pagerState.pageCount - 1) {
                            changeCurrentPage(currentPage + 1) // Переход на следующую страницу
                            offsetX = 0f // Сброс смещения для правильного позиционирования
                        } else if (offsetX < -300 && currentPage > 0) {
                            changeCurrentPage(currentPage - 1) // Переход на предыдущую страницу
                            offsetX = 0f // Сброс смещения
                        }
                    }
                }
                .clickable { onClick() },
        shape = RoundedCornerShape(15.dp)
    ) {
        Column {
            Text(modifier = Modifier.padding(start = 15.dp), text = taskItem.title)
            Spacer(modifier = Modifier.height(15.dp))
            Text(modifier = Modifier.padding(start = 15.dp), text = taskItem.content)
        }
    }
}

fun CloseModalSheetList(
    showBottomSheet: MutableState<Boolean>,
    taskName: MutableState<String>,
    taskContent: MutableState<String>,
) {
    showBottomSheet.value = false
    taskName.value = ""
    taskContent.value = ""
}
