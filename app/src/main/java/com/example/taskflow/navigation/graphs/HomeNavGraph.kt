package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskflow.data.TaskModel
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.RootGraph
import com.example.taskflow.screens.Home
import com.example.taskflow.services.firebase.UserService
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.viewmodels.IncomingNotificationsViewModel

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToRegisterScreen: () -> Unit,
) {
    val incomingNotificationsViewModel = hiltViewModel<IncomingNotificationsViewModel>()
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

        incomingNotificationsNavigationGraph(
            padding = padding,
            navController = navController,
            incomingNotificationsViewModel = incomingNotificationsViewModel,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )

        userProfileNavigationGraph(
            padding = padding,
            navController = navController,
            userService = userService,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )

        taskInfoNavigationGraph(
            padding = padding,
            navController = navController,
            taskModel = taskModel,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )
    }
}
