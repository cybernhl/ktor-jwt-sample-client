package com.example.ktor_jwt_demo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun Home(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state, viewModel){
        viewModel.result.collect{
            it.onFailure {
                snackbarHostState.showSnackbar(it.message ?: "Unknown Error")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Home", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = {
                viewModel.getSecret()
            }) {
                Text(text = "获取受保护的资源（身份验证）")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = {
                viewModel.getSecretWithoutToken()
            }) {
                Text(text = "获取受保护的资源（无身份验证）")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = {
                viewModel.logout()
                navController.navigate("auth")
            }) {
                Text(text = "登出")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "受包含资源: ${state.value.secret}")
        }
    }
}