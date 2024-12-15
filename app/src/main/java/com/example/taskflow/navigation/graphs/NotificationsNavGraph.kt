package com.example.taskflow.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.taskflow.navigation.models.BottomBarGraph
import com.example.taskflow.navigation.models.IncomingNotificationsGraph
import com.example.taskflow.screens.IncomingNotifications.IncomingNotificationsScaffold
import com.example.taskflow.viewmodels.IncomingNotificationsViewModel

fun NavGraphBuilder.incomingNotificationsNavigationGraph(
    padding: PaddingValues,
    navController: NavHostController,
    incomingNotificationsViewModel: IncomingNotificationsViewModel,
    navigateToRegisterScreen: () -> Unit,
) {
    navigation(
        route = BottomBarGraph.INCOMING_NOTIFICATIONS,
        startDestination = IncomingNotificationsGraph.INCOMING_NOTIFICATIONS_START
    ) {
        composable(route = IncomingNotificationsGraph.INCOMING_NOTIFICATIONS_START) {
            IncomingNotificationsScaffold(padding, incomingNotificationsViewModel)
        }
    }
}
