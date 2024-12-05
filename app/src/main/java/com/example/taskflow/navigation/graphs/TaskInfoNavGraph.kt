package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.taskflow.data.TaskModel
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.TaskInfoGraph
import com.example.taskflow.screens.TaskInfoScaffold
import com.example.taskflow.screens.Tasks

fun NavGraphBuilder.taskInfoNavigationGraph(
    padding: PaddingValues,
    navController: NavHostController,
    taskModel: MutableState<TaskModel>,
    navigateToRegisterScreen: () -> Unit,
) {
    navigation(route = BottomBarGraph.TASKS, startDestination = TaskInfoGraph.TASK_START) {
        composable(route = TaskInfoGraph.TASK_START) {
            Tasks(
                padding = padding,
                onClick = arrayOf({ navController.navigate(TaskInfoGraph.TASK_INFO) }),
                taskModel = taskModel
            )
        }

        composable(route = TaskInfoGraph.TASK_INFO) {
            TaskInfoScaffold(padding = padding, taskInfo = taskModel)
        }
    }
}
