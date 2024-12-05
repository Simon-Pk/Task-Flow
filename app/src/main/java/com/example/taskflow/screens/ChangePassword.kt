package com.example.taskflow.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.showError
import com.example.taskflow.services.showSuccess
import com.google.firebase.auth.EmailAuthProvider
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.data.TextConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ChangePassword(padding: PaddingValues) {
    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SinglenessOutlinedTextField(text = oldPassword, label = "Old password", isHiddenText = true)
        SinglenessOutlinedTextField(text = newPassword, label = "New password", isHiddenText = true)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SimpleButton(
            text = "Изменить",
            textConfig = TextConfig(size = 14.sp, align = TextAlign.Center)
        ) {
            scope.launch(Dispatchers.Main) {
                if (UpdatePassword(oldPassword.value, newPassword.value)) {
                    snackBarHostState.showSuccess(message = "Пароль изменен")
                } else {
                    snackBarHostState.showError(message = "Ошибка изменения пароля")
                }
            }
        }
    }
    SnackBar(snackBarHostState = snackBarHostState)
}

suspend fun UpdatePassword(oldPassword: String, newPassword: String): Boolean {
    val credential = EmailAuthProvider.getCredential(getUser()?.email.toString(), oldPassword)
    return try {
        getUser()?.reauthenticate(credential)?.await()
        getUser()?.updatePassword(newPassword)?.await()
        true
    } catch (e: Exception) {
        false
    }
}
