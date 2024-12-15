package com.example.taskflow.screens.IncomingNotifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun IncomingNotifications(
    padding: PaddingValues,
    incomingNotificationsViewModel: IncomingNotificationsViewModel
) {
    val notificationsList =
        incomingNotificationsViewModel.notificationsList.collectAsStateWithLifecycle().value
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(60.dp)) }

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
