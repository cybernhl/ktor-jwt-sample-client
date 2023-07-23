package com.example.ktor_jwt_demo.ui.auth

data class AuthState(
    val title: String = "登入",
    val switchButtonText: String = "注冊",
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
)
