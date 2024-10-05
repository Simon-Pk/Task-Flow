package com.example.taskflow.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.taskflow.R
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.data.TextConfig

@Composable
fun Welcome(navigateToRegistrationScreen: () -> Unit, navigateToLoginScreen: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.image_5x_3),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SimpleButton(
            text = "Вход",
            textConfig = TextConfig.Small,
            shape = RoundedCornerShape(100)
        ) {
            navigateToLoginScreen()
        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SimpleButton(
            text = "Регистрация",
            textConfig = TextConfig.Small,
            shape = RoundedCornerShape(100)
        ) {
            navigateToRegistrationScreen()
        }
    }
}
