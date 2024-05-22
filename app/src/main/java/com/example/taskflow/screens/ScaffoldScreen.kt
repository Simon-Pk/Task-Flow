package com.example.taskflow.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.navigation.graphs.HomeScreenNavGraph
import com.example.taskflow.navigation.models.BottomBarGraph
import com.ravenzip.workshop.components.BottomNavigationBar
import com.ravenzip.workshop.data.BottomNavigationItem
import com.ravenzip.workshop.data.IconParameters

@Composable
fun ScaffoldScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                buttonsList = generateMenuItems(),
                showLabelOnlyOnSelected = true
            )
        }
    ) {
        HomeScreenNavGraph(
            navController = navController,
            padding = it,
        )
    }
}

@Composable
private fun generateMenuItems(): List<BottomNavigationItem> {
    val homeButton =
        BottomNavigationItem(
            label = "Главная",
            route = BottomBarGraph.HOME,
            icon = IconParameters(value = Icons.Outlined.Home),
            hasNews = false
        )

    val tasksButton =
        BottomNavigationItem(
            label = "Мои задачи",
            route = BottomBarGraph.TASKS,
            icon = IconParameters(value = Icons.Outlined.TaskAlt),
            hasNews = false
        )

    val notificationsButton =
        BottomNavigationItem(
            label = "Входящие",
            route = BottomBarGraph.INCOMINGNOTIFICATIONS,
            icon = IconParameters(value = Icons.Outlined.Notifications),
            hasNews = false
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Аккаунт",
            route = BottomBarGraph.USER_PROFILE,
            icon = IconParameters(value = Icons.Outlined.Person),
            hasNews = false
        )

    return listOf(homeButton, tasksButton, notificationsButton, userProfileButton)
}
