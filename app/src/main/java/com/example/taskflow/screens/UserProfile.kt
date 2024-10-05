package com.example.taskflow.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.firebase.logout
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserProfile(
    padding: PaddingValues,
    vararg onClick: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
) {
    val context = LocalContext.current
    val userFirebase = getUser()
    val scope = rememberCoroutineScope()
    val openURLPolicy = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://taskflow.tilda.ws/policy"))
    }
    val openURLuseragreement = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse("http://taskflow.tilda.ws/useragreement"))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        //        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Профиль",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(0.9f),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            //            Box(
            //                modifier =
            //                    Modifier.clip(RoundedCornerShape(50))
            //                        .background(color = MaterialTheme.colorScheme.background)
            //            ) {
            //                Image(
            //                    imageVector = Icons.Outlined.AccountCircle,
            //                    contentDescription = "",
            //                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            //                    modifier = Modifier.size(50.dp)
            //                )
            //            }
            //            Text(
            //                text = userFirebase?.email.toString(),
            //                modifier = Modifier.padding(start = 20.dp),
            //                fontSize = 18.sp,
            //            )

        }
        RowIconButton(
            text = userFirebase?.email.toString(),
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.AccountCircle),
            iconConfig = IconConfig.Primary,
        ) {
            onClick[0]()
        }
        Spacer(modifier = Modifier.height(40.dp))
        //        Row(verticalAlignment = Alignment.CenterVertically) {
        //            Text(text = "Темная тема")
        //            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        //            Switch(checked = checked, onCheckedChange = { checked = it })
        //        }
        RowIconButton(
            text = "Политика конфиденциальности",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Policy),
            iconConfig = IconConfig.Primary
        ) {
            context.startActivity(openURLPolicy)
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = "Пользовательское соглашение",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Handshake),
            iconConfig = IconConfig.Primary
        ) {
            context.startActivity(openURLuseragreement)
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = "Версия приложения\n" + "Pre-Alpha",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.Outlined.Smartphone),
            iconConfig = IconConfig.Primary
        ) {}
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = "Выход",
            textConfig = TextConfig(size = 19.sp),
            icon = Icon.ImageVectorIcon(Icons.AutoMirrored.Outlined.Logout),
            iconConfig = IconConfig.Primary
        ) {
            scope.launch(Dispatchers.Main) {
                logout()
                navigateToRegisterScreen()
            }
        }
    }
}
