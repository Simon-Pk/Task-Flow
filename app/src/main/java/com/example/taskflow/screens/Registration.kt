package com.example.taskflow.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.services.ValidationService
import com.example.taskflow.services.firebase.createUserWithEmail
import com.example.taskflow.services.firebase.reloadUser
import com.example.taskflow.services.showError
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessTextField
import com.ravenzip.workshop.components.SnackBar
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.TextParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Registration(navigateToHomeScreen: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }
    val isPasswordValid = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(false) }
    val spinnerText = remember { mutableStateOf("Регистрация...") }
    val validationService = ValidationService()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SinglenessTextField(text = email, label = "Email")
        SinglenessTextField(text = password, label = "Password", isHiddenText = true)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SimpleButton(text = TextParameters("Зарегистрироваться", size = 14)) {
            scope.launch(Dispatchers.Main) {
                isEmailValid.value = validationService.isEmailValid(email.value)
                isPasswordValid.value = validationService.isPasswordValid(password.value)

                if (!isEmailValid.value) {
                    snackBarHostState.showError("Некорректный email")
                    return@launch
                }
                //                isLoading.value = true
                if (!isPasswordValid.value) {
                    snackBarHostState.showError("В пароле должно быть больше 5 символов")
                    return@launch
                }
                isLoading.value = true

                val isReloadSuccess = reloadUser()
                if (isReloadSuccess.value != true) {
                    isLoading.value = false
                    snackBarHostState.showError(isReloadSuccess.error!!)
                    return@launch
                }

                spinnerText.value = "Регистрация..."
                val authResult = createUserWithEmail(email.value, password.value)

                if (authResult.value == null) {
                    isLoading.value = false
                    snackBarHostState.showError(authResult.error!!)
                    return@launch
                }

                isLoading.value = false
                navigateToHomeScreen()
            }
        }
    }
    if (isLoading.value) {
        Spinner(text = TextParameters(value = spinnerText.value, size = 16))
    }
    SnackBar(snackBarHostState = snackBarHostState)
}
