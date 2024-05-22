package com.example.taskflow.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.services.ValidationService
import com.example.taskflow.services.firebase.reloadUser
import com.example.taskflow.services.firebase.sendPasswordResetEmail
import com.example.taskflow.services.showError
import com.example.taskflow.services.showSuccess
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ForgotPassword() {
    val email = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val validationService = ValidationService()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SinglenessTextField(text = email, label = "Email")
        Spacer(modifier = Modifier.padding(top = 5.dp))
        SimpleButton(
            text = TextParameters("Сбросить пароль", size = 14),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
        ) {
            scope.launch(Dispatchers.Main) {
                isEmailValid.value = validationService.isEmailValid(email.value)
                if (!isEmailValid.value) {
                    snackBarHostState.showError("Проверьте правильность заполнения поля")
                    return@launch
                }

                val isReloadSuccess = reloadUser()
                if (isReloadSuccess.value != true) {
                    snackBarHostState.showError(isReloadSuccess.error!!)
                    return@launch
                }

                val resetResult = sendPasswordResetEmail(email.value)

                if (resetResult) {
                    snackBarHostState.showSuccess(
                        "Письмо со ссылкой для сброса было успешно отправлено на почту"
                    )
                } else {
                    snackBarHostState.showError("Ошибка сброса пароля")
                }
            }
        }
    }
    SnackBar(snackBarHostState = snackBarHostState)
}
