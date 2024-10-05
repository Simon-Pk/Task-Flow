package com.example.taskflow.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskflow.navigation.models.AuthGraph
import com.example.taskflow.navigation.models.RootGraph
import com.example.taskflow.screens.ScaffoldScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination
    ) {
        authNavigationGraph(navController = navController)
        composable(route = RootGraph.MAIN) {
            ScaffoldScreen(
                navigateToRegisterScreen = {
                    navController.navigate(AuthGraph.WELCOME) {
                        popUpTo(RootGraph.ROOT) { inclusive = true }
                    }
                }
            )
        }
    }
}
