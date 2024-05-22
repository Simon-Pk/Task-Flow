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
import androidx.compose.ui.unit.dp
import com.example.taskflow.services.firebase.getUser
import com.example.taskflow.services.showError
import com.example.taskflow.services.showSuccess
import com.google.firebase.auth.EmailAuthProvider
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ChangeEmail(padding: PaddingValues) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SinglenessTextField(text = password, label = "Password", isHiddenText = true)
        SinglenessTextField(text = email, label = "Email", isHiddenText = false)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SimpleButton(text = TextParameters("Изменить", size = 14)) {
            scope.launch(Dispatchers.Main) {
                if (UpdateEmail(email.value, password.value)) {
                    snackBarHostState.showSuccess(
                        message = "Письмо с подтверждением было отправлено на указанный email"
                    )
                } else {
                    snackBarHostState.showError(message = "Ошибка изменения email")
                }
            }
        }
    }
    SnackBar(snackBarHostState = snackBarHostState)
}

suspend fun UpdateEmail(email: String, password: String): Boolean {
    val credential = EmailAuthProvider.getCredential(getUser()?.email.toString(), password)
    return try {
        getUser()?.reauthenticate(credential)?.await()
        getUser()?.verifyBeforeUpdateEmail(email)?.await()
        true
    } catch (e: Exception) {
        false
    }
}
