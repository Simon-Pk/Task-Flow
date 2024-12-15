package com.example.taskflow.screens

import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.data.TaskModel
import com.example.taskflow.viewmodels.HomeViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(padding: PaddingValues, homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()) {
    val taskList = homeViewModel.taskList.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }
    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                homeViewModel.updateListTask(homeViewModel.currentUser?.uid.toString(), "")
                delay(1.seconds)
                isRefreshing.value = false
            }
        },
        modifier = Modifier.padding(padding),
        state = refreshState,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = "Диаграмма статусов задач",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (taskList.isNotEmpty()) {
                item { TaskStatusBarChart(tasks = taskList, homeViewModel = homeViewModel) }
            } else {
                item { Text(text = "Задачи отсутствуют", fontSize = 16.sp) }
            }
        }
    }
}

@Composable
fun TaskStatusBarChart(tasks: List<TaskModel>, homeViewModel: HomeViewModel) {
    // Группируем задачи по статусу
    val taskStatusCount = homeViewModel.groupTasksByStatus(tasks)
    val scope = rememberCoroutineScope()
    // Преобразуем данные для графика
    val entries =
        taskStatusCount.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }
    val isLoading = remember(taskStatusCount) { mutableStateOf(true) }
    val labels = remember { mutableListOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    // Настраиваем легенду статусов
    LaunchedEffect(isLoading.value) {
        if (isLoading.value) {
            labels.clear()
            labels.addAll(homeViewModel.getStatus(taskStatusCount))
            isLoading.value = false
        }
    }

    if (entries.isNotEmpty()) {
        AndroidView(
            modifier = Modifier.fillMaxWidth().height(600.dp),
            factory = { ctx ->
                BarChart(ctx).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                }
            },
            update = { chart ->
                val dataSet =
                    BarDataSet(entries, "Количество задач").apply {
                        colors = ColorTemplate.MATERIAL_COLORS.take(labels.size).toList()
                        valueTextSize = 12f
                    }

                chart.data = BarData(dataSet)
                chart.xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    setLabelRotationAngle(45f)
                }
                chart.xAxis.textColor = primaryColor
                chart.axisLeft.axisMinimum = 0f // Устанавливаем минимальное значение
                chart.axisLeft.textColor = primaryColor
                chart.axisRight.isEnabled = false // Отключаем правую ось
                chart.description.isEnabled = false // Отключаем описание
                chart.legend.apply { verticalAlignment = Legend.LegendVerticalAlignment.TOP }
                chart.legend.textColor = primaryColor
                chart.setFitBars(true) // Включаем отступы для столбцов
                chart.invalidate() // Обновляем график
            }
        )
    } else {
        Text(text = "Нет данных для отображения графика", modifier = Modifier.padding(16.dp))
    }
}
