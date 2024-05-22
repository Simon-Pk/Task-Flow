package com.example.taskflow.navigation.graphs

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.taskflow.navigation.models.AuthGraph
import com.example.taskflow.navigation.models.RootGraph
import com.example.taskflow.screens.Authorization
import com.example.taskflow.screens.ForgotPassword
import com.example.taskflow.screens.Registration
import com.example.taskflow.screens.Welcome

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        composable(route = AuthGraph.WELCOME) {
            Welcome(
                navigateToRegistrationScreen = { navController.navigate(AuthGraph.REGISTRATION) },
                navigateToLoginScreen = { navController.navigate(AuthGraph.LOGIN) },
            )
        }
        composable(route = AuthGraph.REGISTRATION) {
            Registration(navigateToHomeScreen = { navigateToHome(navController) })
        }
        composable(route = AuthGraph.LOGIN) {
            Authorization(
                navigateToHomeScreen = { navigateToHome(navController) },
                navigateToForgotPassScreen = { navController.navigate(AuthGraph.FORGOT_PASS) }
            )
        }
        composable(route = AuthGraph.FORGOT_PASS) { ForgotPassword() }
    }
}

private fun navigateToHome(navController: NavHostController) {
    // Для того, чтобы перейти на главный экран и при этом невозможно было вернуться назад
    navController.navigate(RootGraph.MAIN) {
        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    }
}
