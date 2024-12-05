package com.example.taskflow.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.data.User
import com.example.taskflow.services.firebase.UserService
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.showError
import com.example.taskflow.services.showSuccess
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.data.TextConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChangeName(padding: PaddingValues, userService: MutableState<UserService>) {
    val userData = remember { mutableStateOf(User()) }
    userData.value = userService.value.dataUser.collectAsState().value
    val name = remember { mutableStateOf("") }
    name.value = userData.value.name
    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val isLoadingUser = remember { mutableStateOf(true) }
    LaunchedEffect(isLoadingUser.value) {
        if (isLoadingUser.value) {
            userService.value.get(getUser())
            isLoadingUser.value = false
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
        SinglenessOutlinedTextField(text = name, label = "Имя пользователя")
        Spacer(modifier = Modifier.height(40.dp))
        SimpleButton(
            text = "Сохранить",
            textConfig = TextConfig(size = 19.sp),
        ) {
            scope.launch(Dispatchers.Main) {
                if (name.value == "") {
                    snackBarHostState.showError("Проверьте правильность заполнения полей")
                    return@launch
                }
                if (userService.value.update(userData = userData.value, name = name.value)) {
                    userService.value.get(getUser())
                    snackBarHostState.showSuccess(message = "Данные пользователя успешно обновлены")
                } else {
                    snackBarHostState.showError(message = "Ошибка при обновлении данных")
                }
            }
        }
    }
    SnackBar(snackBarHostState = snackBarHostState)
}
