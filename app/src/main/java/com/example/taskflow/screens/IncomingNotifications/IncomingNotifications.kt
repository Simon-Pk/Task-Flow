package com.example.taskflow.screens.IncomingNotifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.viewmodels.IncomingNotificationsViewModel
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingNotifications(
    padding: PaddingValues,
    incomingNotificationsViewModel: IncomingNotificationsViewModel
) {
    val notificationsList =
        incomingNotificationsViewModel.notificationsList.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                incomingNotificationsViewModel.updateNotificationsList()
                delay(1.seconds)
                isRefreshing.value = false
            }
        },
        modifier = Modifier.padding(padding),
        state = refreshState,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //            item { Spacer(modifier = Modifier.height(60.dp)) }

            items(notificationsList) { notification ->
                //            InfoCard(
                //                icon = Icon.ImageVectorIcon(Icons.Filled.ModeComment),
                //                iconConfig = IconConfig.Small,
                //                text = notification.content,
                //                textConfig = TextConfig.Small,
                //                title = notification.date,
                //                titleConfig = TextConfig.Small,
                //            )
                NotificationCard(
                    icon = Icon.ImageVectorIcon(Icons.Filled.ModeComment), // Замените на ваш ресурс
                    title = notification.date,
                    message = notification.content
                )
            }
        }
    }
}

@Composable
fun NotificationCard(
    icon: Icon, // Ресурс иконки
    title: String, // Заголовок
    message: String, // Текст уведомления
    colors: CardColors = CardDefaults.cardColors() // цвет карточки
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = colors
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Верхняя строка: Иконка и заголовок
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Icon(icon = icon, iconConfig = IconConfig.Small, defaultColor = colors.contentColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Нижний текст уведомления
            Text(
                text = message,
                style =
                    MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, color = Color.Gray)
            )
        }
    }
}
