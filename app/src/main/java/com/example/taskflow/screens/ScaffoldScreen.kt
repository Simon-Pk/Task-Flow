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
import com.ravenzip.workshop.data.appbar.BottomNavigationItem
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@Composable
fun ScaffoldScreen(
    navController: NavHostController = rememberNavController(),
    navigateToRegisterScreen: () -> Unit
) {
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
            navigateToRegisterScreen = navigateToRegisterScreen,
        )
    }
}

@Composable
private fun generateMenuItems(): List<BottomNavigationItem> {
    val homeButton =
        BottomNavigationItem(
            label = "Главная",
            route = BottomBarGraph.HOME,
            icon = Icon.ImageVectorIcon(Icons.Outlined.Home),
            iconConfig = IconConfig.Primary,
            hasNews = false
        )

    val tasksButton =
        BottomNavigationItem(
            label = "Мои задачи",
            route = BottomBarGraph.TASKS,
            icon = Icon.ImageVectorIcon(Icons.Outlined.TaskAlt),
            iconConfig = IconConfig.Primary,
            hasNews = false
        )

    val notificationsButton =
        BottomNavigationItem(
            label = "Входящие",
            route = BottomBarGraph.INCOMING_NOTIFICATIONS,
            icon = Icon.ImageVectorIcon(Icons.Outlined.Notifications),
            iconConfig = IconConfig.Primary,
            hasNews = false
        )

    val userProfileButton =
        BottomNavigationItem(
            label = "Аккаунт",
            route = BottomBarGraph.USER_PROFILE,
            icon = Icon.ImageVectorIcon(Icons.Outlined.Person),
            iconConfig = IconConfig.Primary,
            hasNews = false
        )

    return listOf(homeButton, tasksButton, notificationsButton, userProfileButton)
}
