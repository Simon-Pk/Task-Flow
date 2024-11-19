package com.example.taskflow.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.TaskModel
import com.example.taskflow.viewmodels.TaskViewModel
import com.ravenzip.workshop.components.ChipRadioGroup
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.data.icon.Icon
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tasks(padding: PaddingValues, taskViewModel: TaskViewModel = hiltViewModel<TaskViewModel>()) {
    val Titles = listOf("Недавно назначенные", "В работе", "Ожидание обратной связи", "Выполненные")
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val taskName = remember { mutableStateOf("") }
    val taskContent = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    var dateResult by remember { mutableStateOf("No date") }
    val prioritiesChips = taskViewModel.prioritiesChips.collectAsStateWithLifecycle().value
    // Переменная для хранения смещения
    Box(modifier = Modifier.padding(padding)) {
        Column {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.wrapContentWidth(),
                edgePadding = 16.dp
            ) {
                Titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.scrollToPage(index) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            HorizontalPager(state = pagerState) { pageIndex ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = 1) { taskIndex ->
                        TaskCard(
                            taskItem = TaskModel("Задача ${taskIndex + 1}", "Содержание задачи"),
                            pagerState = pagerState,
                            onPageChange = { newPageIndex ->
                                scope.launch { pagerState.animateScrollToPage(newPageIndex) }
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 10.dp, bottom = 10.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Добавить")
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                // Sheet content

                Column(modifier = Modifier.padding(padding)) {
                    SinglenessTextField(text = taskName, label = "name")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text("Приоритет:")
                    ChipRadioGroup(list = prioritiesChips)
                    Spacer(modifier = Modifier.height(5.dp))
                    //                    Text("Сложность:")
                    //                    ChipRadioGroup(list = list) { item ->
                    //                        list.replaceAll { it.copy(isSelected = it.text ==
                    // item.text) }
                    //                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    MultilineTextField(text = taskContent, label = "content")
                    Spacer(modifier = Modifier.height(5.dp))
                    //                    OutlinedButton(onClick = { openDialog.value = true }) {
                    // Text(dateResult) }
                    //                    if (openDialog.value) {
                    //                        val datePickerState = rememberDatePickerState()
                    //                        val confirmEnabled = derivedStateOf {
                    //                            datePickerState.selectedDateMillis != null
                    //                        }
                    //
                    //                        DatePickerDialog(
                    //                            onDismissRequest = { openDialog.value = false },
                    //                            confirmButton = {
                    //                                TextButton(
                    //                                    onClick = {
                    //                                        openDialog.value = false
                    //                                        var date = "No selection"
                    //                                        if (datePickerState.selectedDateMillis
                    // != null) {
                    //                                            date =
                    //                                                convertLongToDate(
                    //
                    // datePickerState.selectedDateMillis!!
                    //                                                )
                    //                                        }
                    //                                        dateResult = date
                    //                                    },
                    //                                    enabled = confirmEnabled.value
                    //                                ) {
                    //                                    Text(text = "Okay")
                    //                                }
                    //                            }
                    //                        ) {
                    //                            DatePicker(state = datePickerState)
                    //                        }
                    //                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(taskItem: TaskModel, pagerState: PagerState, onPageChange: (Int) -> Unit) {
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
                            onPageChange(currentPage + 1) // Переход на следующую страницу
                            offsetX = 0f // Сброс смещения для правильного позиционирования
                        } else if (offsetX < -300 && currentPage > 0) {
                            onPageChange(currentPage - 1) // Переход на предыдущую страницу
                            offsetX = 0f // Сброс смещения
                        }
                    }
                },
        shape = RoundedCornerShape(15.dp)
    ) {
        Column {
            Text(modifier = Modifier.padding(5.dp), text = taskItem.title)
            Spacer(modifier = Modifier.height(15.dp))
            Text(modifier = Modifier.padding(5.dp), text = taskItem.content)
        }
    }
}
