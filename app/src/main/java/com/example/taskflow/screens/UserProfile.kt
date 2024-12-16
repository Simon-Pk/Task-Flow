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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.data.Priority
import com.example.taskflow.data.Status
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.firebase.logout
import com.example.taskflow.viewmodels.ProfileViewModel
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(
    padding: PaddingValues,
    vararg onClick: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
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

    var showBottomSheet1 by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val priorityName = remember { mutableStateOf("") }
    val priorityValue = remember { mutableStateOf("") }
    val priorityCode = remember { mutableStateOf("") }

    val statusName = remember { mutableStateOf("") }
    val statusCode = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        //        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = true
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Профиль",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(0.9f),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {}
        }
        item {
            RowIconButton(
                text = userFirebase?.email.toString(),
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.AccountCircle),
                iconConfig = IconConfig.Primary,
            ) {
                onClick[0]()
            }
            Spacer(modifier = Modifier.height(40.dp))
        }

        //        Row(verticalAlignment = Alignment.CenterVertically) {
        //            Text(text = "Темная тема")
        //            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        //            Switch(checked = checked, onCheckedChange = { checked = it })
        //        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RowIconButton(
                text = "Создать приоритет",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.Policy),
                iconConfig = IconConfig.Primary
            ) {
                showBottomSheet1 = true
            }
        }

        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RowIconButton(
                text = "Создать статус",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.Policy),
                iconConfig = IconConfig.Primary
            ) {
                showBottomSheet2 = true
            }
        }

        item {
            if (showBottomSheet1) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet1 = false
                        priorityName.value = ""
                        priorityValue.value = ""
                        priorityCode.value = ""
                    },
                    sheetState = sheetState,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SinglenessOutlinedTextField(text = priorityName, label = "name")

                        Spacer(modifier = Modifier.height(5.dp))

                        SinglenessOutlinedTextField(text = priorityValue, label = "value")

                        Spacer(modifier = Modifier.height(5.dp))

                        SinglenessOutlinedTextField(text = priorityCode, label = "code")

                        Spacer(modifier = Modifier.height(5.dp))

                        SimpleButton(
                            text = "Создать",
                            textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
                        ) {
                            if (
                                priorityCode.value != "" &&
                                    priorityName.value != "" &&
                                    priorityValue.value != ""
                            ) {
                                profileViewModel.createPriority(
                                    Priority(
                                        "",
                                        priorityCode.value.toInt(),
                                        priorityName.value,
                                        priorityValue.value.toInt()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))

            if (showBottomSheet2) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet2 = false
                        statusName.value = ""
                    },
                    sheetState = sheetState,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SinglenessOutlinedTextField(text = statusName, label = "name")

                        Spacer(modifier = Modifier.height(5.dp))

                        SinglenessOutlinedTextField(text = statusCode, label = "code")

                        Spacer(modifier = Modifier.height(5.dp))

                        SimpleButton(
                            text = "Создать",
                            textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
                        ) {
                            if (statusName.value != "") {

                                profileViewModel.createStatus(
                                    Status(
                                        "",
                                        statusName.value,
                                        statusCode.value.toInt(),
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))

            RowIconButton(
                text = "Политика конфиденциальности",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.Policy),
                iconConfig = IconConfig.Primary
            ) {
                context.startActivity(openURLPolicy)
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RowIconButton(
                text = "Пользовательское соглашение",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.Handshake),
                iconConfig = IconConfig.Primary
            ) {
                context.startActivity(openURLuseragreement)
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RowIconButton(
                text = "Версия приложения\n" + "Pre-Alpha",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
                icon = Icon.ImageVectorIcon(Icons.Outlined.Smartphone),
                iconConfig = IconConfig.Primary
            ) {}
        }
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
            RowIconButton(
                text = "Выход",
                textConfig = TextConfig(size = 19.sp, align = TextAlign.Center),
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
}
