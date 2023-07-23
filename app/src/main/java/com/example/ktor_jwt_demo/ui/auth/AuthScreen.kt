package com.example.ktor_jwt_demo.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun Auth(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(state, viewModel){
        viewModel.authResult.collect{
            it.onSuccess {
                navController.navigate("home")
            }.onFailure {
                snackbarHostState.showSnackbar(it.message ?: "Unknown Error")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(64.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.value.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = state.value.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = { Text(text = "用户名") }
                )
                TextField(
                    value = state.value.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text(text = "密码") }
                )
                Row {
                    Button(onClick = { viewModel.switchMode() }) {
                        Text(text = "切换到${state.value.switchButtonText}")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        when (state.value.title) {
                            "登入" -> viewModel.signIn()
                            "注册" -> viewModel.signUp()
                        }
                        focusManager.clearFocus()
                    }) {
                        Text(text = state.value.title)
                    }
                }
            }
        }
    }
}