package com.example.taskflow.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig

@Composable
fun SettingsScreen(padding: PaddingValues, vararg onClick: () -> Unit) {
    Spacer(modifier = Modifier.padding(top = 10.dp))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RowIconButton(
            text = "Сменить имя пользователя",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Badge),
            iconConfig = IconConfig.Primary
        ) {
            onClick[2]()
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = "Сменить email",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Mail),
            iconConfig = IconConfig.Primary
        ) {
            onClick[1]()
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = "Сменить пароль",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Key),
            iconConfig = IconConfig.Primary
        ) {
            onClick[0]()
        }
    }
}
