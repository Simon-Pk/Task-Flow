package com.example.taskflow.screens.IncomingNotifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskflow.viewmodels.IncomingNotificationsViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun IncomingNotificationsScaffold(
    padding: PaddingValues,
    incomingNotificationsViewModel: IncomingNotificationsViewModel,
) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Notifications", backArrow = null, items = listOf()) },
    ) { innerPadding ->
        IncomingNotifications(
            padding = innerPadding,
            incomingNotificationsViewModel = incomingNotificationsViewModel
        )
    }
}
