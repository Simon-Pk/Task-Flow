package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.RootGraph
import com.example.taskflow.screens.Home
import com.example.taskflow.screens.IncomingNotifications
import com.example.taskflow.screens.Tasks

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
) {

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) { Home(padding = padding) }

        composable(route = BottomBarGraph.TASKS) { Tasks(padding) }

        composable(route = BottomBarGraph.INCOMINGNOTIFICATIONS) { IncomingNotifications(padding) }

        userProfileNavigationGraph(padding, navController = navController)
    }
}
