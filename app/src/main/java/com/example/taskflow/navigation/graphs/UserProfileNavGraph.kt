package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.UserProfileGraph
import com.example.taskflow.screens.ChangeEmail
import com.example.taskflow.screens.ChangePassword
import com.example.taskflow.screens.SettingsScreen
import com.example.taskflow.screens.UserProfile

fun NavGraphBuilder.userProfileNavigationGraph(
    padding: PaddingValues,
    navController: NavHostController,
) {
    navigation(
        route = BottomBarGraph.USER_PROFILE,
        startDestination = UserProfileGraph.USER_PROFILE_START
    ) {
        composable(route = UserProfileGraph.USER_PROFILE_START) {
            UserProfile(
                padding = padding,
                onClick = arrayOf({ navController.navigate(UserProfileGraph.SETTINGS_USER) })
            )
        }
        composable(route = UserProfileGraph.SETTINGS_USER) {
            SettingsScreen(
                padding = padding,
                onClick =
                    arrayOf(
                        { navController.navigate(UserProfileGraph.CHANGEPASSWORD) },
                        { navController.navigate(UserProfileGraph.CHANGEEMAIL) }
                    )
            )
        }
        composable(route = UserProfileGraph.CHANGEPASSWORD) { ChangePassword(padding = padding) }
        composable(route = UserProfileGraph.CHANGEEMAIL) { ChangeEmail(padding = padding) }
    }
}
