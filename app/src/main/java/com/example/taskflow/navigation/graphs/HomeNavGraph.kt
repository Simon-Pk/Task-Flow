package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskflow.data.TaskModel
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.RootGraph
import com.example.taskflow.screens.Home
import com.example.taskflow.screens.IncomingNotifications
import com.example.taskflow.services.firebase.UserService
import com.example.taskflow.services.firebase.getUser

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToRegisterScreen: () -> Unit,
) {
    val userService = remember { mutableStateOf(UserService()) }
    val taskModel = remember { mutableStateOf(TaskModel()) }
    val isLoadingUser = remember { mutableStateOf(true) }
    LaunchedEffect(isLoadingUser.value) {
        if (isLoadingUser.value) {
            userService.value.get(getUser())
            isLoadingUser.value = false
        }
    }
    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) { Home(padding = padding) }

        composable(route = BottomBarGraph.INCOMINGNOTIFICATIONS) { IncomingNotifications(padding) }

        userProfileNavigationGraph(
            padding,
            navController = navController,
            userService = userService,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )

        taskInfoNavigationGraph(
            padding,
            navController = navController,
            taskModel = taskModel,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )
    }
}
