package com.example.taskflow.screens

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.firebase.logout
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.IconParameters
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserProfile(padding: PaddingValues, vararg onClick: () -> Unit) {
    val context = LocalContext.current
    val userFirebase = getUser()
    val scope = rememberCoroutineScope()
    //    val isThemedark = isSystemInDarkTheme()
    //    var checked by remember { mutableStateOf(isThemedark) }
    val openURLPolicy =
        Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://taskflow.tilda.ws/policy"))
    val openURLuseragreement =
        Intent(
            android.content.Intent.ACTION_VIEW,
            Uri.parse("http://taskflow.tilda.ws/useragreement")
        )

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
            text = TextParameters(userFirebase?.email.toString(), 19),
            icon = IconParameters(Icons.Outlined.AccountCircle, description = "")
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
            text = TextParameters("Политика конфиденциальности", 19),
            icon = IconParameters(Icons.Outlined.Policy)
        ) {
            context.startActivity(openURLPolicy)
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = TextParameters("Пользовательское соглашение", 19),
            icon = IconParameters(Icons.Outlined.Handshake)
        ) {
            context.startActivity(openURLuseragreement)
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text = TextParameters("Версия приложения\n" + "Pre-Alpha", 19),
            icon = IconParameters(Icons.Outlined.Smartphone)
        ) {}
        Spacer(modifier = Modifier.padding(top = 10.dp))
        RowIconButton(
            text =
                TextParameters(
                    "Выход",
                    19,
                ),
            icon = IconParameters(Icons.AutoMirrored.Outlined.Logout)
        ) {
            scope.launch(Dispatchers.Main) {
                val packageManager: PackageManager = context.packageManager
                val intent: Intent? = packageManager.getLaunchIntentForPackage(context.packageName)
                val componentName: ComponentName? = intent?.component
                val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)
                logout()
                var timer = 2
                while (timer != 0) {
                    delay(1000)
                    timer -= 1
                }
                context.startActivity(mainIntent)
                Runtime.getRuntime().exit(0)
            }
        }
    }
}
