package com.example.taskflow.screens

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.unit.dp
import com.example.taskflow.TaskModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tasks(padding: PaddingValues) {
    val Titles = listOf("Недавно назначенные", "В работе", "Ожидание обратной связи", "Выполненные")
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val dragBoxIndex = remember { mutableIntStateOf(0) }
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
        //
        HorizontalPager(state = pagerState) { state ->
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize()
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target =
                                remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            val text =
                                                event
                                                    .toAndroidDragEvent()
                                                    .clipData
                                                    ?.getItemAt(0)
                                                    ?.text
                                            dragBoxIndex.value = state
                                            return true
                                        }
                                    }
                                }
                        )
            ) {
                items(count = 2) {
                    TaskCard(TaskModel("Задача ${it+1}", "Содержание задачи"), state, dragBoxIndex)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(TaskItem: TaskModel, index: Int, dragBoxIndex: MutableIntState) {
    Card(
        modifier =
            Modifier.fillMaxWidth().padding(10.dp).dragAndDropSource {
                detectTapGestures(
                    onLongPress = { offset ->
                        startTransfer(
                            transferData =
                                DragAndDropTransferData(
                                    clipData = ClipData.newPlainText("text", "")
                                )
                        )
                    }
                )
            },
        shape = RoundedCornerShape(15.dp)
    ) {
        AnimatedVisibility(
            visible = index == dragBoxIndex.value,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Column() {
                Text(modifier = Modifier.padding(5.dp), text = TaskItem.title)
                Spacer(modifier = Modifier.height(15.dp))
                Text(modifier = Modifier.padding(5.dp), text = TaskItem.content)
            }
        }
    }
}
